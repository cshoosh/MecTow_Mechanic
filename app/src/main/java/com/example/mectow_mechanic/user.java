package com.example.mectow_mechanic;

    public class user {
        private String mech_name;
        private String mech_email;
        private String mech_phonenumber;
        private String mech_password;
        private String mech_confirm_password;

        public user() {

        }

        public user(String name, String email, String phonenumber, String password, String confirm_password) {
            this.mech_name = name;
            this.mech_email = email;
            this.mech_phonenumber = phonenumber;
            this.mech_password = password;
            this.mech_confirm_password = confirm_password;
        }
        public String getName() {
            return mech_name;
        }

        public void setName(String name) {
            this.mech_name = name;
        }

        public String getEmail() {
            return mech_email;
        }

        public void setEmail(String email) {
            this.mech_email = email;
        }

        public String getPhonenumber() {
            return mech_phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.mech_phonenumber = phonenumber;
        }

        public String getPassword() {
            return mech_password;
        }

        public void setPassword(String password) {
            this.mech_password = password;
        }

        public String getConfirm_password() {
            return mech_confirm_password;
        }

        public void setConfirm_password(String confirm_password) {
            this.mech_confirm_password = confirm_password;
        }
    }

