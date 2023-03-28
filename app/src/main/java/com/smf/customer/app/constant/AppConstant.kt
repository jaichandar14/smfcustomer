package com.smf.customer.app.constant

object AppConstant {
    const val AUTHORIZATION = "authorization"
    const val EXTRA = "EXTRA"

    // 3247
    const val DEV = "dev"
    const val QA = "qa"
    const val UAT = "uat"
    const val ENCODE = "UTF-8"
    const val EMAIL = "Email"
    const val MOBILE = "Mobile"
    const val EVENT_ORGANIZER = "EVENT_ORGANIZER"
    const val ERROR_MESSAGE = "errorMessage"
    const val MESSAGE_CODE = "messageCode"
    const val INVALID_USER = "Invalid User"
    const val INTERNET_ERROR = "InternetError"

    // CODE
    val SUCCESS_CODE = 200
    val FORBIDDEN = 403
    val BAD_REQUEST = 400
    val NOT_FOUND = 404
    val UNAUTHORISED = 401
    val TIMEOUT_EXCEPTION = 440
    val TOO_MANY_REQUEST_CODE = 429

    val SERVICE_UNAVAILABLE = 503
    val SERVICE_UNAVAILABLE_2 = 502
    val INTERNAL_SERVER_ERROR_CODE = 500
    val OTP_SENT_CODE = 20022
    val OTP_NOT_SENT_CODE = 20023
    val OTP_ALREADY_SENT_CODE = 20024
    val OTP_EXPIRED = 20025
    val INVALID_OTP = 20026
    val OTP_MATCH_CODE = 20027
    val NEGATIVE_CODE = 500

    // 3245
    const val INITIAL_TIME = " 00:00"
    const val RESEND_OTP = "Resend OTP"
    const val EMAIL_VERIFICATION_CODE_PAGE = "goToEmailVerificationCodePage"
    const val EMAIL_VERIFIED_TRUE_GOTO_DASHBOARD = "EMailVerifiedTrueGoToDashBoard"
    const val INVALID_OTP_TEXT = "Invalid Otp"
    const val FALSE = "false"
    const val ENTER_OTP = "Enter OTP"
    const val SIGN_IN_NOT_COMPLETED = "SignInNotCompleted"
    const val SIGN_IN_COMPLETED_GOTO_DASH_BOARD = "signInCompletedGoToDashBoard"
    const val BEARER = "Bearer"
    const val ERROR_CODE_N = "ER_022"
    const val ERROR_CODE = "errorCode"

    // 3265
    const val EVENT_QUESTIONS_DIALOG = "EventQuestionsDialog"
    const val DATE_FORMAT = "MM/dd/yyyy"
    const val MATERIAL_DATE_PICKER = "materialDatePicker"
    const val MATERIAL_TIME_PICKER = "materialTimePicker"
    const val COUNTRY = "country"
    const val STATES = "states"
    const val COUNTRIES = "countries"

    //3273
    const val TITLE = "title"
    const val TEMPLATE_ID = "templateId"
    const val SUBMIT = "Submit"
    const val CANCEL = "Cancel"

    const val EDIT_BUTTON = "editButton"
    const val CLOSE = "Close"
    const val VIEW_QUESTIONS_DIALOG = "ViewQuestionsDialog"
    const val MESSAGE = "message"

    // 3285
    const val UNDER_REVIEW = "UNDER REVIEW"
    const val REVOKED = "REVOKED"
    const val REJECTED = "REJECTED"
    const val PENDING_ADMIN_APPROVAL = "PENDING ADMIN APPROVAL"
    const val NEW = "NEW"
    const val DELETED = "DELETED"
    const val CLOSED = "CLOSED"
    const val APPROVED = "APPROVED"
    const val ROTATED = "ROTATED"
    const val MonthDate = "MMM dd"
    const val ACTIVE = "Active"
    const val PENDING = "Pending"
    const val DRAFT = "Draft"
    const val REJECT = "Reject"
    const val CLOSED_TXT = "Closed"

    // 3306
    const val ON_EVENT="onEvent"

    //3317
    const val EVENT_DASH_BOARD= "EventDashBoard"
    const val EVENT_QUESTIONS= "EventQuestions"
    const val SERVICE_QUESTIONS= "ServiceQuestions"
    const val EVENT_DETAILS_ACTIVITY= "EventDetailsActivity"
    const val PROVIDE_SERVICE_DETAILS_ACTIVITY= "ProvideServiceDetailsActivity"
    const val SELECTED_ANSWER_MAP= "selectedAnswerMap"
    const val QUESTION_LIST_ITEM= "questionListItem"
    const val QUESTION_NUMBER_LIST= "questionNumberList"
    const val FROM_ACTIVITY= "fromActivity"
    const val QUESTION_BTN_TEXT= "QuestionButtonText"
    const val QUESTION_BUTTON= "QuestionButton"

    //3372
    const val PLAIN_SNACK_BAR = "Plain Snackbar"

    // 3421
    const val MILE_SPINNER = "MileSpinner"
    const val TIME_SLOT = "TimeSlot"
    const val ZIPCODE = "ZipCode"
    const val NULL = "null"

    // 3429
    const val LAYOUTONE=0
    const val LAYOUTTWO=1
    const val EVENT_ID="Event_Id"
    const val EVENT_SERVICE_ID="Event_ServiceId"
    const val SERVICE_CATEGORY_ID="serviceCategoryId"
    const val EVENT_SERVICE_DESCRIPTION_ID="Event_ServiceDescriptionId"
    const val LEAD_PERIOD="Lead_Period"


}