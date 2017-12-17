package com.jianchen.rentme.helper;

/**
 * Created by emerald on 5/30/2017.
 */
public class Constants {
    public static final String FIREBASE_ROOT = "https://rentme-189218.firebaseio.com/";

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
    public static final int REQUEST_GOOGLE_SIGNIN = 0xA;
    public static final int REQUEST_PREVIEW = 0xB;
    public static final int REQUEST_REVIEW = 0xC;


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
    public static final String EXTRA_ACTION_TYPE = "ACTION_TYPE";

    public static final int MAX_SKILLS = 5;
    public static final int SEARCH_RADIUS = 5000;
    /*
    public static final String SITE_URL = "http://192.168.0.204/";
    public static final String API_ROOT_URL = "http://192.168.0.204/rentme/index.php/";
    */

    public static final String SITE_URL = "http://34.203.243.197/rentme/";
    public static final String API_ROOT_URL = "http://34.203.243.197/rentme/index.php/";

    public static final String API_IMAGE_UPLOAD = "common/uploadImage";
    public static final String API_USER_LOGIN = "user/loginUser";
    public static final String API_USER_SIGNUP = "user/signupUser";
    public static final String API_USER_FORGOT = "user/forgotPassword";
    public static final String API_USER_REGISTER = "user/registerUser";
    public static final String API_USER_EDIT = "user/editUserProfile";
    public static final String API_USER_SEARCH_BY_LOCATION = "user/searchByLocation";
    public static final String API_USER_SHARE_LOCATION = "user/shareLocation";
    public static final String API_USER_GET_BY_ID = "user/getUserById";
    public static final String API_USER_SERVICE = "user/getUserServices";
    public static final String API_USER_SKILL = "user/getUserSkills";
    public static final String API_SERVICE_REVIEW = "service/getServiceReviews";
    public static final String API_SERVICE_CREATE = "service/createService";
    public static final String API_SERVICE_DELETE = "service/deleteService";
    public static final String API_PROJECT_CREATE = "project/createProject";
    public static final String API_PROJECT_CREATE2 = "project/createProject2";
    public static final String API_PROJECT_COMPLETED = "project/getMyCompletedProjects";
    public static final String API_PROJECT_REVIEW = "project/getProjectReview";
    public static final String API_REVIEW_PROJECT = "project/reviewProject";
    public static final String API_PROJECT_INPROGRESS = "project/getMyProgressProjects";
    public static final String API_PROJECT_CHATTING = "project/getChattingProject";
    public static final String API_PROJECT_FINISH = "project/completeProject";
    public static final String API_REVIEW_CREATE = "review/createReview";
    public static final String API_RATE_CREATE = "rate/createRate";
    public static final String API_REVIEW_REVIEW = "review/getReviewReviews";
    public static final String API_SKILL_GET = "skill/getCategories";
    public static final String API_COMMON_UPLOAD_VIDEO = "common/uploadVideos";
    public static final String API_COMMON_UPLOAD_WEB = "common/uploadWebs";
    public static final String API_COMMON_UPLOAD_IMAGE = "common/imageMultiUpload";
    public static final String API_USER_PROFILEIMAGE = "user/getProfileImagesById";
    public static final String API_COMMON_UPDATE_SUBIMAGE = "common/updateSubProfileImages";

    // Social Login
    public static final String API_USER_GOOGLE_CHECK = "user/checkGoogleUser";
    public static final String API_USER_FACEBOOK_CHECK = "user/checkFacebookUser";



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

    public static final int ACTION_CREATE_PROJECT = 1027;
    public static final int ACTION_CREATE_SERVICE = 1028;

    public static final int VALUE_SERVICE = 0;
    public static final int VALUE_REVIEW = 1;

    public static final int VALUE_REVIEW_PHOTO = 4;
    public static final int VALUE_PROFILEMAIN_PHOTO = 1;
    public static final int VALUE_PROFILESUB_PHOTO = 2;
    public static final int VALUE_SERVICE_PHOTO = 3;

    public static final int VALUE_LINK_YOUTUBE = 0;
    public static final int VALUE_LINK_VIMEO = 1;

    public static final int VALUE_IMG_USER_COVER = 0;
    public static final int VALUE_IMG_USER_MAIN = 1;
    public static final int VALUE_IMG_SERVICE = 2;
    public static final int VALUE_IMG_SKILL = 3;

    public static final int MODE_ADD = 0;
    public static final int MODE_SHOW = 1;

    public static final String KEY_USER = "key_user";
    public static final String KEY_VIDEO = "key_video";
    public static final String KEY_WEB = "key_web";
    public static final String KEY_REVIEW_ID = "key_review_id";
    public static final String KEY_SERVICE = "key_service";
    public static final String KEY_SKILL = "key_skill";
    public static final String KEY_SUB_IMAGE = "key_sub_image";
    public static final String KEY_SUB_POSITION = "key_sub_position";

    public static final String KEY_TRANSITION = "key_transition";

    public static final String LOGINMODE_FACEBOOK = "FACEBOOK";
    public static final String LOGINMODE_GOOGLE = "GOOGLE";
    public static final String LOGINMODE_EMAIL = "EMAIL";


}
