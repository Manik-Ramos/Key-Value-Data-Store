package com.freshworks.constants;

public class FreshworksConstants {

	private String keyStorePath = System.getProperty("user.home")+"//Desktop//DataStore.txt";

	private String jsonPath = System.getProperty("user.home")+"//Desktop//JsonRead.json";
	
	public static String INVALID_FILE_PATH = System.getProperty("user.home")+"//Desktop//?DataStore.txt"; 
	
	public static String ASK_FOR_FILEPATH = "Enter File path for your Datastore";
	
	public static String FILE_ALREADY_EXISTS_ERROR = "We have identified that a file already exists at this location!!\nPlease change the location or proceed with accessing the file in the directory";
	
	public static String NEW_KEYSTORE_LOCATION_MESSAGE = "New Datastore file is being created at ";
	
	public static String CHOOSE_OPERATION_MESSAGE = "Please Select the action you want to perform\nPlease enter the respective keywords for the actions\nCreate - C\nRead - R\nDelete - D";
	
	public static String INVALID_OPERATION_ERROR = "You have performed an invalid Operation\nPlease perform one of the operations\nCreate - C\nRead - R\nDelete - D";
	
	public static String CREATE_OPERATION = "C";
	
	public static String READ_OPERATION = "R";
	
	public static String DELETE_OPERATION = "D";
	
	public static String INVALID_FILE_PATH_FOR_KEYSTORE = "The path provided seems to be invalid\nPlease enter a valid path";
	
	public static String ASK_FOR_KEY = "Please enter a key for the datastore";
	
	public static String KEY_LENGTH_EXCEEDED_ERROR = "Key must not contain more than 32 characters";
	
	public static String KEY_EXISTS_ERROR = "\nKey Already Exists";
	
	public static String INVALID_KEY = "Invalid Key";
	
	public static String ASK_FOR_TIME_TO_LIVE = "\nDo you want to enter time to live, Yes/No";
	
	public static String ASK_FOR_TIME_TO_LIVE_IN_MINUTES = "Please Enter the time in minutes";
	
	public static String TIME_TO_LIVE_AVAILABLE = "timeToLiveAvailable";
	
	public static String SECONDS = "seconds";
	
	public static String CREATED_TIME= "createdTime";
	
	public static String TIME_TO_LIVE = "timeToLive";
	
	public static String ASK_FOR_JSON = "Please enter the json";
	
	public static String CREATION_SUCCESS= "Create Operation Success";
	
	public static String CREATION_FAILED = "Create Operation Failed";
	
	public static String INVALID_JSON = "Not a valid json";
	
	public static String JSON_SIZE_EXCEEDED = "Sorry! The JSON used is above 16Kb,please a smaller sized JSON";
	
	public static String READ_OPERATION_FAILED = "Read Operation Succeeded";
	
	public static String KEY_DOES_NOT_EXIST ="Key Does Not Exist, Please use a valid key to read...";
	
	public static String TIME_TO_AVAILABLE_AS_YES = "\"timeToLiveAvailable\":\"yes\"";
	
	public static String TIME_TO_AVAILABLE_AS_NO = "\"timeToLiveAvailable\":\"no\"";
	
	public static String TIME_TO_AVAILABLE_AS_SECONDS = "\"seconds\":";
	
	public static String TIME_TO_AVAILABLE_AS_CREATED_TIME = "\"createdTime\":";
	
	public static String SEPARATOR = ":";
	
	public static int GET_CREATED_TIME = 14;
	
	public static String READABLE_TIME_LEFT_MESSAGE = "You can read this key for another ";
	
	public static String MINUTES = " minute(s)";
	
	public static String KEY_FOR_DELETE_DOES_NOT_EXIST = "Check the key you have entered as it does not Exist";
	
	public static String DELETION_SUCCESS= "Delete Operation Succeeded";
	
	public static String DELETION_FAILED = "Delete Operation Failed";
	
	public static String KEYSTORE = "Keystore";
	
	public static String DELETE_EXCEPTION = "Exception in Delete";
	
	public static String READ_EXCEPTION = "Exception in Read";
	
	public static String KEY_EXPIRATION_PREFIX = "The Key ";
	
	public static String KEY_EXPIRATION_SUFFIX = " is not available for reading as it has expired";
	
	public static int BYTE_SIZE = 1024;//In Bytes
	
	public static int MAXIMUM_JSON_SIZE = 16;//In KBs
	
	public static int BYTE_CONVERSION_FOR_GIGABYTES = 1073741824;
	
	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	
}
