package com.freshworks.datastore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.freshworks.constants.FreshworksConstants;

public class DataStore {
	
	private static FreshworksConstants constants = new FreshworksConstants();
	
	public static String yesOrNo = "No";
	
	public static Integer timeToLive = 0;
	
	public static Long createdTime = 0L;
	
	public static Long timeToLiveInSeconds = 0L;
	
	@SuppressWarnings({ "resource", "static-access" })
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println(constants.ASK_FOR_FILEPATH);
		String filePath = sc.nextLine();
				
		//Check if it is a valid Path
		boolean validFilePath = isFilePathValid(filePath);
		
		if(validFilePath){
			filePath = setFixedFilePath(filePath);
			
			File file = new File(filePath);
			try{
				//If file exists already, reuse it
				if(file.exists()) {
					 System.err.println(constants.FILE_ALREADY_EXISTS_ERROR);					 
				} else {
					//Else Create New File
					file.createNewFile();
					System.out.println(constants.NEW_KEYSTORE_LOCATION_MESSAGE+file.getAbsolutePath());					
				}
				
				System.out.println(constants.CHOOSE_OPERATION_MESSAGE);
				
				boolean validAction = false;
				String action = "";
				
				while(!validAction) {
					action = sc.next();
					//Check if the user wants to perrform either of Create, Read or Delete
					if(!"".equals(action) && null != action) {
						validAction = checkIfValidOperation(action);
					}
					
					//Restrict further action if an invalid operation is performed
					if(!validAction)
						System.err.println(constants.INVALID_OPERATION_ERROR);
				}
				
				//once valid Operation is performed, route to the respective operation
				if(validAction) {
					String key = getKeyFromUser();
					String message = "";
					if(action.toUpperCase().startsWith(constants.CREATE_OPERATION)) {
						message = createEntry(file,key);
					} else if(action.toUpperCase().startsWith(constants.READ_OPERATION)) {
						message = readEntry(key);
					} else if(action.toUpperCase().startsWith(constants.DELETE_OPERATION)) {
						message = deleteEntry(key);
					}
					displayMessage(message);
				}
			}catch(Exception e){
				 e.printStackTrace();
			}
		} else {
			System.err.println(constants.INVALID_FILE_PATH_FOR_KEYSTORE);
		}
	}
	
	public static boolean isFilePathValid(String filePath) {
		try{
			Paths.get(filePath);
		} catch(InvalidPathException e){
			return false;
		}
		return true;
	}

	public static String setFixedFilePath(String filePath) {
		//If file path is invalid use default path
		if("".equalsIgnoreCase(filePath.trim())) {
			filePath = constants.getKeyStorePath();
		} else {
			constants.setKeyStorePath(filePath);
		}
		return filePath;
	}
	
	@SuppressWarnings("static-access")
	public static boolean checkIfValidOperation(String actionPerformed){
		//valid Operation the user is allowed to perform
		List<String> validOperations = Arrays.asList(constants.CREATE_OPERATION,constants.READ_OPERATION,constants.DELETE_OPERATION);			
		
		return validOperations.contains(actionPerformed.toUpperCase());
	}

	@SuppressWarnings({ "static-access", "resource" })
	public static String getKeyFromUser() {
		//Read the key from user
		Scanner sc = new Scanner(System.in);
		System.out.println(constants.ASK_FOR_KEY);
		String key = sc.nextLine();
		
		return key;
	}
	
	@SuppressWarnings("static-access")
	public static String createEntry(File file,String key) throws FileNotFoundException, IOException, ParseException {		
		
		//validating if the user string is not empty
		if(validateString(key)) {					
			
			//Validating the default key size is capped within 32 characters
			if(validateKeySize(key)) {
				System.err.println(constants.KEY_LENGTH_EXCEEDED_ERROR);
				return constants.KEY_LENGTH_EXCEEDED_ERROR;
			} else {
				//Getting the list of available keys already in the store
				List<String> listOfKeys = getKeysFromKeystore();
				
				//Validating if the key already exists in the store
				boolean keyExist = validateExistenceOfKeyInStore(key,listOfKeys);
				
				if(!keyExist) {
					//If key doesn't exist get the time to Live Property if needed
					JSONObject timeToLiveObject = getTimeToLiveProperty();
					
					//Creating Value if the key is within the limit and unique
					return obtainUserJSON(file, key,timeToLiveObject,constants.getJsonPath());
				} else {
					//If key exists already in the store restrict creation
					System.err.println(constants.KEY_EXISTS_ERROR);
					return constants.KEY_EXISTS_ERROR;
				}
			}
		}
		return constants.INVALID_KEY;
	}

	public static boolean validateKeySize(String key) {
		if(validateString(key))
			return key.length() > 32;//maximum limit of key size
		else
			return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<String> getKeysFromKeystore() throws FileNotFoundException, IOException, ParseException {
		List<String> keys = new LinkedList<>();
		
		//Reading the available keys in the keystore
		Properties properties = getProperties();
			
		for(Map.Entry entry: properties.entrySet()) 
			keys.add(entry.getKey().toString());
		return keys;
	}
	
	private static Properties getProperties() throws FileNotFoundException, IOException {
		//Reading the already available key values in the store
		FileReader reader = new FileReader(constants.getKeyStorePath());
		Properties properties = new Properties();
		properties.load(reader);
		return properties;
	}
	
	public static boolean validateExistenceOfKeyInStore(String key,List<String> listOfKeyValues) {
		return listOfKeyValues.contains(key);		
	}
	
	@SuppressWarnings({ "unchecked", "resource", "static-access" })
	public static JSONObject getTimeToLiveProperty() throws IOException {
		
		Scanner sc = new Scanner(System.in);
		JSONObject timeToLiveObject = new JSONObject();
				
		JSONObject timeToLiveInternalObject = new JSONObject();
		
		timeToLive = 0;
		
		System.out.println(constants.ASK_FOR_TIME_TO_LIVE);
		
		yesOrNo = sc.nextLine();
		
		//Getting the value if the user wants to set Time to Live Property
		if("Yes".equalsIgnoreCase(yesOrNo.trim())){
			System.out.println(constants.ASK_FOR_TIME_TO_LIVE_IN_MINUTES);
			
			String time = sc.nextLine();
			String []str = time.split(" ");
			time = str[0];
			timeToLive = Integer.parseInt(time) * 60;
			createdTime = System.currentTimeMillis() / 1000;
		}
		
		//Forming an object of the time properties
		timeToLiveInternalObject.put(constants.TIME_TO_LIVE_AVAILABLE, yesOrNo.toLowerCase());
		timeToLiveInternalObject.put(constants.SECONDS, timeToLive);
		timeToLiveInternalObject.put(constants.CREATED_TIME, createdTime);
		
		timeToLiveObject.put(constants.TIME_TO_LIVE, timeToLiveInternalObject);
		
		return timeToLiveObject;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	private static String obtainUserJSON(File keyStore,String key,JSONObject timeToLiveObject,String jsonPath) throws IOException, ParseException, FileNotFoundException {
		JSONObject object = new JSONObject();
										
		System.out.println(constants.ASK_FOR_JSON);
		
		//Validating the size of json if it is less than 16 KBs
		if(validateSizeOfJson(jsonPath)) {
			//If it is valid get the json from the jsonPath default location
			Object jsonValue = readJson(jsonPath);
			
			if(Objects.nonNull(jsonValue) && jsonValue instanceof Map) {					
				object.put(constants.TIME_TO_LIVE,timeToLiveObject.get(constants.TIME_TO_LIVE));
				
				object.putAll((Map)jsonValue);
				
				//Once it satisfies the provided conditions en entry in the key store is created
				createKeyValuePair(key,object,keyStore.getAbsolutePath());

				//Giving the user Informations about the operation
				return getProperties().keySet().contains(key) ? constants.CREATION_SUCCESS : constants.CREATION_FAILED ;
			} else {
				return constants.INVALID_JSON;
			}
		} else {
			//If the size of json is greater than 16 Kbs restrict creation of entry in the key store
			System.err.println(constants.JSON_SIZE_EXCEEDED);
			return constants.JSON_SIZE_EXCEEDED;
		}
	}

	public static Object readJson(String jsonPath) throws IOException, ParseException, FileNotFoundException {
		
		//Reading file from the path
		
		System.out.println(jsonPath);
		Object jsonValue = new JSONParser().parse(new FileReader(jsonPath));
		return jsonValue;
	}

	@SuppressWarnings("static-access")
	public static boolean validateSizeOfJson(String path) {
		File file = new File(path);
		
		//Validating json size if it is less than 16 KBs
		Double jsonSize = Double.valueOf(file.length()) / constants.BYTE_SIZE;
		
		boolean isJsonSizeValid = jsonSize < constants.MAXIMUM_JSON_SIZE  ? true : false;
		
		//If the json size is valid, checking if addition of json to the keystore will exceed the maximum keystore size of 1 GB 
		if(isJsonSizeValid) {
			boolean isKeyStoreSizeValid = validateSizeOfKeystore(constants.getKeyStorePath(),jsonSize);
			
			//If both the size of json and store are valid, allowing creation of entry to the store
			return isKeyStoreSizeValid && isJsonSizeValid;
		}
					
		return false;
	}
	
	@SuppressWarnings("static-access")
	public static boolean validateSizeOfKeystore(String keyStorePath,Double jsonSize) {
		File file = new File(keyStorePath);
		return (Double.valueOf(file.length() + jsonSize) / constants.BYTE_CONVERSION_FOR_GIGABYTES) < 1  ? true : false;
	}
	
	public static void createKeyValuePair(String key,JSONObject value,String filePath) throws IOException, ParseException {
		
		//Creating an entry in the keystore
		FileWriter writer = new FileWriter(filePath,true);
		writer.append(key+"="+value);
		writer.append("\n");
		writer.flush();
		writer.close();
	}

	//Read Operations
	@SuppressWarnings("static-access")
	public static String readEntry(String key) {
		try {
			
			String object = "";
			//Getting the list of keys already in the store
			List<String> listOfKeys = getKeysFromKeystore();
			
			//Checking if the list contains this key
			if(!listOfKeys.isEmpty() && listOfKeys.contains(key)) {
				
				//validating the time to live property of the key and checking if it can be readable
				boolean readable = checkIfKeyIsReadable(key);
				
				if(readable) {
					//If the key is readable then the key based values are read from the store
					object = getValueFromKeystoreBasedOnKey(key);
					System.out.println(object);
					return constants.READ_OPERATION_FAILED;
				} else {
					return constants.KEY_EXPIRATION_PREFIX + key + constants.KEY_EXPIRATION_SUFFIX;
				}
			} else {
				//System.err.println("Key Does Not Exist, Please use a valid key to read...");
				return constants.KEY_DOES_NOT_EXIST;
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return constants.READ_EXCEPTION;
		}
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static String getValueFromKeystoreBasedOnKey(String key) {
		
		JSONObject object = new JSONObject();
		List<JSONObject> objects = new LinkedList<>();
		
		Properties properties = new Properties();
		
		try {
			//Getting all the keys and filter the respective key the user needs
			properties = getProperties();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Filteration of the respective key
		try {
			for(Map.Entry entry : properties.entrySet()) {
				if(entry.getKey().equals(key)) {					
					object.put(entry.getKey(), entry.getValue());
					break;
				} 
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return object.toJSONString().replaceAll("\\\\", "");
	}
	
	@SuppressWarnings("static-access")
	public static boolean checkIfKeyIsReadable(String key) throws FileNotFoundException, IOException, ParseException {

		//Getting the already available keys
		Properties properties = getProperties();
		
		//Getting the keys values
		String keyResult = null != properties.get(key) ? properties.get(key).toString() : "";
		
		//Checking the values of the resultant values
		if(validateString(keyResult)) {
			boolean isYes = false;
			
			//Checking if the time to live is yes
			int timeToLiveAvailable = keyResult.indexOf(constants.TIME_TO_AVAILABLE_AS_YES);
			
			//Checking if it has no, if it doesnt have yes
			if(timeToLiveAvailable == -1) {
				timeToLiveAvailable = keyResult.indexOf(constants.TIME_TO_AVAILABLE_AS_NO);
			} else {
				isYes = true;
			}
			
			if(isYes) {
				//Getting the already entered seconds
				int seconds = keyResult.indexOf(constants.TIME_TO_AVAILABLE_AS_SECONDS);
				
				String[] second = (keyResult.substring(seconds,timeToLiveAvailable-1)).split(constants.SEPARATOR);
				
				int realSecond = Integer.parseInt(second[1]);
				
				//Getting the time of creation of the key
				int createdTimeInSeconds = keyResult.indexOf(constants.TIME_TO_AVAILABLE_AS_CREATED_TIME);
				
				String createdSecond = "";
							
				//Getting the created time
				while(Character.isDigit(keyResult.charAt(createdTimeInSeconds + constants.GET_CREATED_TIME))) {
					createdSecond = createdSecond + keyResult.charAt(createdTimeInSeconds + constants.GET_CREATED_TIME);
					createdTimeInSeconds++;
				}
							
				int realCreatedSecond = Integer.parseInt(createdSecond);
				
				if(System.currentTimeMillis()/1000 - realCreatedSecond <= realSecond) {
					//allow to read if current time - createdTime is lesser than the time to live
					long now = System.currentTimeMillis() / 1000;
					long then = realCreatedSecond;
					long still = realSecond - (now - then);

					System.err.println(constants.READABLE_TIME_LEFT_MESSAGE+ still / 60 + constants.MINUTES);
					
					return true;
				} else {
					//delete entry from store
					System.err.println(deleteEntry(key));
					return false;
				}
			} else {
				// Consider, If the Key does not have a time to live property set
				// We cannot determine if it has expired or not, 
				// We can either maintain it in the store for reading only, or delete it from the store.
				// I am just maintaining it without giving the access to read it.			
				
				return false;
			}		
		} 
		return false;
	}
	
	public static boolean validateString(String input) {
		return null != input && !"".equals(input);
	}

	//Delete Operations	
	@SuppressWarnings("static-access")
	public static String deleteEntry(String key) {		
		if(validateKeySize(key)) {
			System.err.println("Invalid Key");
			return constants.INVALID_KEY;
		} else {
			try {
				List<String> keysList = getKeysFromKeystore();
				
				if(!keysList.contains(key)) {
					System.err.println("Check the key you have entered as it does not Exist");
					return constants.KEY_FOR_DELETE_DOES_NOT_EXIST;
				} else {
					boolean successOrFailure = deleteKeyEntry(key);
					return successOrFailure ? constants.DELETION_SUCCESS : constants.DELETION_FAILED;
				}
			} catch (IOException | ParseException e) {
				e.printStackTrace();
				return constants.DELETE_EXCEPTION;
			}
		}
	}
	
	
	public static boolean deleteKeyEntry(String key) {
		boolean isEntryDeleted = false;
		
		try {
			Properties properties = getProperties();
			
			//Removing the particular entry from the keystore
			if(!properties.isEmpty() && properties.keySet().contains(key)){
				properties.remove(key);
				setProperties(properties);
				isEntryDeleted = true;
			} else {
				isEntryDeleted = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return isEntryDeleted;
	}
	
		
	@SuppressWarnings("static-access")
	private static void setProperties(Properties properties) throws IOException {
		
		//Writing result to the store
		FileWriter writer = new FileWriter(constants.getKeyStorePath());
		properties.store(writer, constants.KEYSTORE);
		writer.flush();
		writer.close();
	}
	
	
	private static void displayMessage(String displayText){
		
		//Displaying the user required message
		System.out.println(displayText);
	}	
		
}
