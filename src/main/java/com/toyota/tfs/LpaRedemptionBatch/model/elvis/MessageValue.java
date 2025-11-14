package com.toyota.tfs.LpaRedemptionBatch.model.elvis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageValue {
    private String environment_id;
    private String system_id;
    private String servicer_id;
    private String client_id;
    private int account_id;
    private String borrower_id;
    private String borrower_type_code;
    private String ucid;
    private String work_request_start_date;
    private String work_request_status_date;
    private String work_request_status_code;
    private String work_request_id;
    private String work_request_instance_id;
    private String user_id;
    private String department_id;
    private String grounding_status_code;
    private String grounding_status_date;
    private String grounding_scheduled_date;
    private String grounding_actual_date;
    private int grounding_mileage;
    private String dealer_id;
    private String grounding_location_name;
    private String grounding_location_address_line_1;
    private String grounding_location_address_line_2;
    private String grounding_location_address_line_3;
    private String grounding_location_city;
    private String grounding_location_state;
    private String grounding_location_zip;
    private String grounding_location_country;
    private String grounding_location_contact_name;
    private String grounding_location_phone_nbr;
    private String grounding_location_email_address;
    private String grounding_option_code;
    private String signed_odometer_received_flag;
    private int number_of_keys_returned;
    private String earliest_return_date;
    private String days_between_inspection_and_ground;
    private String last_update_user;
    private String last_update_date;
}
