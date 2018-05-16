package com.amoharib.graduationproject.services;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.amoharib.graduationproject.buyer.activities.MenuActivity;
import com.amoharib.graduationproject.buyer.adapters.MenuAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.models.User;
import com.amoharib.graduationproject.utils.OrderStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by AMoharib on 2018-03-25.
 */

public class DataService {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference addressDB = db.child("address");
    private DatabaseReference orderDB = db.child("order");
    private DatabaseReference userOrdersDB = db.child("user-order");
    private DatabaseReference restOrderDB = db.child("rest-order");
    private DatabaseReference restDB = db.child("restaurants");
    private DatabaseReference usersDB = db.child("users");
    private DatabaseReference categoryDB = db.child("category");
    private DatabaseReference menuDB = db.child("menu");


    private static final DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {
    }

    public void addAddress(String uid, Address address, final DataListeners.DataListener dataListener) {
        DatabaseReference tempDB = addressDB.child(uid).push();
        address.setId(tempDB.getKey());
        tempDB.setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataListener.onReceiveStatus(task.isSuccessful());
            }
        });
    }

    public void getAddresses(String uid, final DataListeners.addAddressListener addressListener) {
        final ArrayList<Address> addresses = new ArrayList<>();

        addressDB.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                    addresses.add(child.getValue(Address.class));
                addressListener.onAddressesReceived(addresses);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                addressListener.onAddressesReceived(null);
            }
        });
    }

    public void deleteAddress(String uid, String id, final int position, final DataListeners.addAddressListener deleteAddressListener) {

        addressDB.child(uid).child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                deleteAddressListener.onAddressDeleted(databaseReference != null, position);
            }
        });
    }

    public void editAddress(String uid, Address newAddress, final DataListeners.addAddressListener addressListener) {
        addressDB.child(uid).child(newAddress.getId()).setValue(newAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addressListener.onAddressEdited(task.isSuccessful());
            }
        });

    }

    public void addOrder(final String restId, final String uid, final String addressID, final ArrayList<CartItem> items, final DataListeners.OnOrderAdditionListener onOrderAdditionListener) {

        DataService.getInstance().getCurrentTime(new DataListeners.CurrentTimeListener() {
            @Override
            public void onTimeReceived(long timestamp) {
                final DatabaseReference tempDB = orderDB.push();

                Order order = new Order(tempDB.getKey(), restId, uid, addressID, items, timestamp);
                tempDB.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onOrderAdditionListener.onOrderAdded(tempDB.getKey(), task.isSuccessful());
                        if (task.isSuccessful()) {
                            userOrdersDB.child(uid).child(tempDB.getKey()).setValue(false);
                            restOrderDB.child(restId).child(tempDB.getKey()).setValue(false);
                        }
                    }
                });
            }
        });


    }

    public void getRestaurant(String restId, final DataListeners.RestaurantListener retrieveDataListener) {
        restDB.child(restId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                retrieveDataListener.onRestaurantRetrieved(restaurant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                retrieveDataListener.onRestaurantRetrieved(null);

            }
        });
    }

    public void addOnOrderStatusChanged(String orderId, final DataListeners.OrderStatusListener orderStatusListener) {

        orderDB.child(orderId).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OrderStatus status = dataSnapshot.getValue(OrderStatus.class);
                orderStatusListener.onStatusChanged(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getAllRestaurants(final DataListeners.OnRestaurantsListener onRestaurantsListener) {

        final ArrayList<Restaurant> restaurants = new ArrayList<>();
        restDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Restaurant restaurant = child.getValue(Restaurant.class);
                    restaurants.add(restaurant);
                }
                onRestaurantsListener.onDataRetrieved(restaurants);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsersOrders(String uid, final DataListeners.OnOrderListener onOrderListener) {

        userOrdersDB.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final ArrayList<Order> orders = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String orderId = child.getKey();
                        orderDB.child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Order order = dataSnapshot.getValue(Order.class);
                                orders.add(order);
                                onOrderListener.onDataReceived(orders);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    onOrderListener.onDataReceived(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getRestaurantOrders(String uid, final DataListeners.OnOrderListener onOrderListener) {

        restOrderDB.child(uid).orderByValue().equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final ArrayList<Order> orders = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String orderId = child.getKey();
                        orderDB.child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Order order = dataSnapshot.getValue(Order.class);
                                orders.add(order);
                                onOrderListener.onDataReceived(orders);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    onOrderListener.onDataReceived(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAddressByUid(String uid, String addressId, final DataListeners.UserAddressListener userAddressListener) {
        addressDB.child(uid).child(addressId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Address address = dataSnapshot.getValue(Address.class);
                userAddressListener.onDataReceived(address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCurrentTime(final DataListeners.CurrentTimeListener currentTimeListener) {
        final DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long offset = snapshot.getValue(Long.class);
                Long estimatedServerTimeMs = System.currentTimeMillis() + offset;
                currentTimeListener.onTimeReceived(estimatedServerTimeMs);
                offsetRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    public void registerNewRestaurant(String username, String password, final DataListeners.RestaurantRegistrationListener registrationListener) {
        String email = username.concat("@firebase.com");
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    registrationListener.onRestaurantRegistered(firebaseUser);
                } else {
                    registrationListener.onRestaurantRegistered(null);
                }
            }
        });
    }

    public void uploadImage(String location, Uri imageUri, final DataListeners.UploadImageListener uploadImageListener) {
        storageReference.child(location).putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                int currentProgress = (int) progress;
                uploadImageListener.onImageUploadProgress(currentProgress);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadImageListener.onImageUploaded(taskSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadImageListener.onImageUploadFailed(e);
            }
        });
    }

    public void addRestaurantToDB(Restaurant restaurant, final DataListeners.DataListener dataListener) {
        restDB.child(restaurant.getId()).setValue(restaurant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataListener.onReceiveStatus(task.isSuccessful());
            }
        });
    }

    public void getUserById(String uid, final DataListeners.UserListener userListener) {
        usersDB.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    userListener.onDataReceived(dataSnapshot.getValue(User.class));
                else
                    userListener.onDataReceived(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addNewUser(final User user, final DataListeners.UserListener userListener) {
        usersDB.child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Registration : " + task.getException().getMessage());
                if (task.isSuccessful())
                    userListener.onDataReceived(user);
                else userListener.onDataReceived(null);
            }
        });
    }

    public void getOrderCountForRestaurant(String restId, final DataListeners.NewOrderNotificationListener newOrderNotificationListener) {
        restOrderDB.child(restId).orderByValue().equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newOrderNotificationListener.onOrderAdded(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getMenuForRestaurantId(final String restId, final DataListeners.OnMenuListener onMenuListener) {


        getCategoriesForRestaurant(restId, new DataListeners.OnCategoryListener() {
            @Override
            public void onCategoriesRetrieved(ArrayList<String> categories) {
                for (final String category : categories) {
                    menuDB.child(restId).child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Food> foods = new ArrayList<>();

                            for (DataSnapshot foodSS : dataSnapshot.getChildren()) {
                                foods.add(foodSS.getValue(Food.class));
                            }

                            onMenuListener.onMenuRetrieved(foods, category);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCategoriesNotFound() {

            }
        });
    }

    public void deleteMenuItemForRestaurant(String restId, String category, String itemId, final DataListeners.DataListener dataListener) {
        menuDB.child(restId).child(category).child(itemId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataListener.onReceiveStatus(task.isSuccessful());
            }
        });
    }

    public void getCategoriesForRestaurant(String restId, final DataListeners.OnCategoryListener onCategoryListener) {
        categoryDB.child(restId).orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> categories = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String category = child.getKey();
                    categories.add(category);
                }
                onCategoryListener.onCategoriesRetrieved(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadFoodItem(final String restId, final String category, boolean isNewCategory, int categoryCount, final Food food, final DataListeners.DataListener dataListener, final DataListeners.UploadImageListener uploadImageListener) {
        if (isNewCategory) {
            categoryDB.child(restId).child(category).setValue(categoryCount - 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        uploadFoodItemToRestaurant(restId, category, food, dataListener, uploadImageListener);
                    }
                }
            });
        } else {
            uploadFoodItemToRestaurant(restId, category, food, dataListener, uploadImageListener);
        }
    }

    private void uploadFoodItemToRestaurant(final String restId, final String category, final Food food, final DataListeners.DataListener dataListener, final DataListeners.UploadImageListener uploadImageListener) {
        final DatabaseReference tempDB = menuDB.child(restId).child(category).push();
        food.setId(tempDB.getKey());
        String location = String.format("%s/menu/%s.jpg", restId, tempDB.getKey());
        if (!food.getIcon().isEmpty()) {
            uploadImage(location, Uri.parse(food.getIcon()), new DataListeners.UploadImageListener() {
                @Override
                public void onImageUploadProgress(int progress) {
                    uploadImageListener.onImageUploadProgress(progress);
                }

                @Override
                public void onImageUploaded(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadImageListener.onImageUploaded(taskSnapshot);
                    food.setIcon(taskSnapshot.getDownloadUrl().toString());
                    uploadFoodItemToRestaurant(tempDB, food, dataListener);
                }

                @Override
                public void onImageUploadFailed(Exception ex) {
                    uploadImageListener.onImageUploadFailed(ex);
                }
            });

        } else {
            uploadFoodItemToRestaurant(tempDB, food, dataListener);
        }
    }

    private void uploadFoodItemToRestaurant(DatabaseReference tempDB, Food food, final DataListeners.DataListener dataListener) {
        tempDB.setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataListener.onReceiveStatus(task.isSuccessful());
            }
        });
    }

    public void updateOrderStatus(Order order, OrderStatus orderStatus, DataListeners.DataListener dataListener) {
        orderDB.child(order.getId()).child("status").setValue(orderStatus);

        if (orderStatus == OrderStatus.OUT) {
            restOrderDB.child(order.getRestId()).child(order.getId()).setValue(true);
            userOrdersDB.child(order.getUserId()).child(order.getId()).setValue(true);
        }

        dataListener.onReceiveStatus(true);
    }

    public void addTokenToUser(String userId, String token) {
        usersDB.child(userId).child("token").setValue(token);
    }
}
