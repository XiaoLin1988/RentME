package com.android.jianchen.rentme.Utils;

/**
 * Created by emerald on 5/30/2017.
 */
public class Constants {
    public static final String FIREBASE_ROOT = "https://rentme-169707.firebaseio.com/";

    public static final String PAYPAL_CLIENT_ID = "Ae1hNYiWsL_zv4WFp3wtt7tKOuIw-9bhm7ETOsRjHjSaDwXxjHypzvBLoe6mKINONdfnepZzRLpVIgvK";
    public static final int PAYPAL_REQUEST_CODE = 2001;

    public static final int MESSAGE_REQUEST = 2000;

    public static final String PREF_CUR_USER = "current_user";

    public static final int REQUEST_GALLERY = 1000;
    public static final int REQUEST_CAMERA = 1001;

    public static final String NEW_OR_EDIT = "NEW_OR_EDIT";

    public static final int IMAGE_MAX_SIZE = 1024;

    public static final String IMAGE_PATH = "image_path";

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
    public static final int REQUEST_CODE_PICK_GALLERY = 0x2;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x3;
    public static final int REQUEST_CODE_CROPPED_PICTURE = 0x4;
    public static final int REQUEST_SERVICE_CREATE = 0x5;
    public static final int REQUEST_POST_PHOTO = 0x6;
    public static final int REQUEST_POST_VIDEO = 0x7;
    public static final int REQUEST_POST_WEB = 0x8;
    public static final int REQUEST_PROJECT_CREATE = 0x9;


    public static final String ERROR_MSG = "error_msg";
    public static final String ERROR = "error";

    public static final String LOG_FILE = "sdcard/log.txt";
    public static final String LOCATION_FILE = "sdcard/location.txt";

    public static final String JSON_REQUEST = "json_obj_request";

    public static final int LOCATION_INTERVAL = 1000 * 60;
    public static final float LOCATION_DISTANCE = 1f;
    public static final String LOCATION_RUNNING = "runningInBackground";
    public static final String APP_NAME = "com.android.emerald.rentme";

    public static final String EXTRA_USERTYPE = "USERTYPE";
    public static final String EXTRA_PROJECT_DETAIL = "PROJECT_DETAIL";
    public static final String EXTRA_SERVICE_DETAIL = "SERVICE_DETAIL";
    public static final String EXTRA_TALENT = "TALENT";
    public static final String EXTRA_PROJECT = "PROJECT";
    public static final String EXTRA_REVIEW_TYPE = "REVIEW_TYPE";

    public static final int MAX_SKILLS = 5;
    public static final int SEARCH_RADIUS = 5000;

    //public static final String SITE_URL = "http://34.203.243.197/";
    public static final String SITE_URL = "http://192.168.0.208/";
    public static final String API_ROOT_URL = "http://192.168.0.208/rentme/index.php/api/";
    //public static final String API_ROOT_URL = "http://34.203.243.197/index.php/api/";
    public static final String API_IMAGE_UPLOAD = "uploadImage";
    public static final String API_USER_LOGIN = "loginUser";
    public static final String API_USER_FORGOT = "forgotPassword";
    public static final String API_USER_EMAIL = "sendEmail";
    public static final String API_USER_REGISTER = "registerUser";
    public static final String API_USER_EDIT = "editUserProfile";
    public static final String API_USER_SEARCH_BY_LOCATION = "searchByLocation";
    public static final String API_USER_SHARE_LOCATION = "shareLocation";
    public static final String API_USER_GET_BY_ID = "getUserById";
    public static final String API_USER_SERVICE = "getUserServices";
    public static final String API_SERVICE_REVIEW = "getServiceReviews";
    public static final String API_SERVICE_CREATE = "createService";
    public static final String API_PROJECT_CREATE = "createProject";
    public static final String API_PROJECT_CREATE2 = "createProject2";
    public static final String API_PROJECT_COMPLETED = "getMyCompletedProjects";
    public static final String API_PROJECT_INPROGRESS = "getMyProgressProjects";
    public static final String API_PROJECT_MYREVIEW = "getMyReviews";
    public static final String API_PROJECT_CHATTING = "getChattingProject";
    public static final String API_PROJECT_FINISH = "completeProject";
    public static final String API_REVIEW_CONSUMER = "eleaveConsumerReview";
    public static final String API_REVIEW_TALENT = "leaveTalentReview";
    public static final String API_REVIEW_CREATE = "createReview";
    public static final String API_RATE_CREATE = "createRate";
    public static final String API_REVIEW_REVIEW = "getReviewReviews";
    public static final String API_SKILL_GET = "getCategories";
    public static final String API_COMMON_UPLOAD_VIDEO = "uploadVideos";
    public static final String API_COMMON_UPLOAD_WEB = "uploadWebs?XDEBUG_SESSION_START=17943";
    public static final String API_COMMON_UPLOAD_IMAGE = "uploadImages";

    public static final int ACTION_CROP = 1000;
    public static final int ACTION_BOOST = 1001;
    public static final int ACTION_BRIGHTNESS = 1002;
    public static final int ACTION_COLORDEPTH = 1003;
    public static final int ACTION_COLORFILTER = 1004;
    public static final int ACTION_CONTRAST = 1005;
    public static final int ACTION_EMBOSS = 1006;
    public static final int ACTION_FLIP = 1007;
    public static final int ACTION_GAMMA = 1008;
    public static final int ACTION_GAUSSIAN = 1009;
    public static final int ACTION_GRAYSCALE = 1010;
    public static final int ACTION_HUE = 1011;
    public static final int ACTION_INVERT = 1012;
    public static final int ACTION_NOISE = 1013;
    public static final int ACTION_SATURATION = 1014;
    public static final int ACTION_SEPIA = 1015;
    public static final int ACTION_SHARPEN = 1016;
    public static final int ACTION_SKETCH = 1017;
    public static final int ACTION_TINT = 1018;
    public static final int ACTION_VIGNETTE = 1019;
    public static final int ACTION_ROTATE_LEFT = 1020;
    public static final int ACTION_ROTATE_RIGHT = 1021;
    public static final int ACTION_FLIP_HORIZONTAL = 1022;
    public static final int ACTION_FLIP_VERTICAL = 1023;
    public static final int ACTION_ORIGINAL = 1024;
    public static final int ACTION_DOODLE = 1025;
    public static final int ACTION_FRAME = 1026;

    public static final int VALUE_SERVICE = 0;
    public static final int VALUE_REVIEW = 1;

    public static final int VALUE_LINK_YOUTUBE = 0;
    public static final int VALUE_LINK_VIMEO = 1;

    public static final int VALUE_IMG_USER_COVER = 0;
    public static final int VALUE_IMG_USER_MAIN = 1;
    public static final int VALUE_IMG_SERVICE = 2;
    public static final int VALUE_IMG_SKILL = 3;

    public static final String KEY_USER = "key_user";
    public static final String KEY_VIDEO = "key_video";
    public static final String KEY_PHOTO = "key_photo";
    public static final String KEY_WEB = "key_web";
    public static final String KEY_REVIEW = "key_review";
    public static final String KEY_SERVICE = "key_service";

    public static final String KEY_TRANSITION = "key_transition";
}
