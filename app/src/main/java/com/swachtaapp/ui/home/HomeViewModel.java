package com.swachtaapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel {

        private String binNumber;
        private String address;
        private String status;
        private String remark;
        private String date;





        public String getBinNumber() {
            return binNumber;
        }

        public void setBinNumber(String binNumber) {
            this.binNumber = binNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
}