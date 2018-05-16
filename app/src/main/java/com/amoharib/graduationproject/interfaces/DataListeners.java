package com.amoharib.graduationproject.interfaces;

import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.models.Food;
import com.amoharib.graduationproject.models.Order;
import com.amoharib.graduationproject.models.Restaurant;
import com.amoharib.graduationproject.models.User;
import com.amoharib.graduationproject.utils.OrderStatus;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public abstract class DataListeners {


    private DataListeners() {
    }

    public interface OnOrderAdditionListener {
        public void onOrderAdded(String orderId, boolean status);
    }

    public interface addAddressListener {

        public void onAddressesReceived(ArrayList<Address> addresses);

        public void onAddressDeleted(boolean status, int position);

        public void onAddressEdited(boolean status);

    }

    public interface DataListener {

        public void onReceiveStatus(boolean status);
    }

    public interface OrderStatusListener {
        public void onStatusChanged(OrderStatus status);
    }

    public interface RestaurantListener {
        public void onRestaurantRetrieved(Restaurant restaurant);
    }

    public interface OnRestaurantsListener {
        public void onDataRetrieved(ArrayList<Restaurant> restaurants);
    }

    public interface OnOrderListener {
        public void onDataReceived(ArrayList<Order> orders);
    }

    public interface UserAddressListener {
        public void onDataReceived(Address address);
    }

    public interface CurrentTimeListener {
        public void onTimeReceived(long timestamp);
    }

    public interface RestaurantRegistrationListener {
        public void onRestaurantRegistered(FirebaseUser firebaseUser);
    }

    public interface UploadImageListener {
        public void onImageUploadProgress(int progress);

        public void onImageUploaded(UploadTask.TaskSnapshot taskSnapshot);

        public void onImageUploadFailed(Exception ex);
    }

    public interface UserListener {
        public void onDataReceived(User user);
    }

    public interface NewOrderNotificationListener {
        public void onOrderAdded(long size);
    }

    public interface OnMenuListener {
        public void onMenuRetrieved(ArrayList<Food> foods, String category);
    }

    public interface OnCategoryListener {
        public void onCategoriesRetrieved(ArrayList<String> categories);
        public void onCategoriesNotFound();
    }
}
