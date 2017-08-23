package com.falconnect.zipcode.Model;

public class RutaPageModel {

    String job_accepted_destination_id;
    String errand;
    String id;
    String user;
    String latitude_desti;
    String longitude_desti;
    String name;
    String address;
    String references;
    String community_desti;
    String self_contact;
    String stnumber;
    String contact_name;
    String phone_number1;
    String phone_number2;
    String email;
    String auto_generated;
    String latitude;
    String longitude;
    String addressee;
    String observation;
    String position;
    String contact_names;
    String phone_numbers1;
    String emails;
    String completed;
    String order_number;
    String tracking_number;
    String was_delivered_to_contact;
    String was_delivered_to_watchman;
    String delivery_remarks;
    String delivery_contact_rut;
    String delivery_contact_name;
    String delivery_contact_phone;



    public RutaPageModel(
            String job_accepted_destination_id,
            String errand,
            String id,
            String user,
            String latitude_desti,
            String longitude_desti,
            String name,
            String address,
            String references,
            String community_desti,
            String self_contact,
            String stnumber,
            String contact_name,
            String phone_number1,
            String phone_number2,
            String email,
            String auto_generated,
            String latitude,
            String longitude,
            String addressee,
            String observation,
            String position,
            String contact_names,
            String phone_numbers1,
            String emails,
            String completed,
            String order_number,
            String tracking_number,
            String was_delivered_to_contact,
            String was_delivered_to_watchman,
            String delivery_remarks,
            String delivery_contact_rut,
            String delivery_contact_name,
            String delivery_contact_phone) {

        this.job_accepted_destination_id = job_accepted_destination_id;
        this.errand = errand;
        this.id = id;
        this.user = user;
        this.latitude_desti = latitude_desti;
        this.longitude_desti = longitude_desti;
        this.name = name;
        this.address = address;
        this.references = references;
        this.community_desti = community_desti;
        this.self_contact = self_contact;
        this.stnumber = stnumber;
        this.contact_name = contact_name;
        this.phone_number1 = phone_number1;
        this.phone_number2 = phone_number2;
        this.email = email;
        this.auto_generated = auto_generated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressee = addressee;
        this.observation = observation;
        this.position = position;
        this.contact_names = contact_names;
        this.phone_numbers1 = phone_numbers1;
        this.emails = emails;
        this.completed = completed;
        this.order_number = order_number;
        this.tracking_number = tracking_number;
        this.was_delivered_to_contact = was_delivered_to_contact;
        this.was_delivered_to_watchman = was_delivered_to_watchman;
        this.delivery_remarks = delivery_remarks;
        this.delivery_contact_rut = delivery_contact_rut;
        this.delivery_contact_name = delivery_contact_name;
        this.delivery_contact_phone = delivery_contact_phone;

    }

    public String getAddress() {
        return address;
    }

    public String getAddressee() {
        return addressee;
    }

    public String getAuto_generated() {
        return auto_generated;
    }

    public String getCommunity_desti() {
        return community_desti;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getEmail() {
        return email;
    }

    public String getErrand() {
        return errand;
    }

    public String getContact_names() {
        return contact_names;
    }

    public String getId() {
        return id;
    }

    public String getJob_accepted_destination_id() {
        return job_accepted_destination_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getEmails() {
        return emails;
    }

    public String getLatitude_desti() {
        return latitude_desti;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLongitude_desti() {
        return longitude_desti;
    }

    public String getCompleted() {
        return completed;
    }

    public String getName() {
        return name;
    }

    public String getObservation() {
        return observation;
    }

    public String getPhone_number1() {
        return phone_number1;
    }

    public String getPhone_number2() {
        return phone_number2;
    }

    public String getReferences() {
        return references;
    }

    public String getPosition() {
        return position;
    }

    public String getSelf_contact() {
        return self_contact;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getPhone_numbers1() {
        return phone_numbers1;
    }

    public String getStnumber() {
        return stnumber;
    }

    public String getUser() {
        return user;
    }

    public String getDelivery_contact_name() {
        return delivery_contact_name;
    }

    public String getDelivery_contact_phone() {
        return delivery_contact_phone;
    }

    public String getDelivery_contact_rut() {
        return delivery_contact_rut;
    }

    public String getDelivery_remarks() {
        return delivery_remarks;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public String getWas_delivered_to_contact() {
        return was_delivered_to_contact;
    }

    public String getWas_delivered_to_watchman() {
        return was_delivered_to_watchman;
    }

}

