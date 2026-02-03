package dbms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class StorageSetup {
	
	// 파일 저장 경로
	// Windows: "C:/BoardStorage/"
	// Linux:   "/home/사용자명/BoardStorage/"
	// Mac:     "/Users/사용자명/BoardStorage/"
	// public static final String SAVE_DIR = "/home/choiy/public_html/BoardStorage/"; // 서버
	// public static final String SAVE_DIR = "C:/BoardStorage/"; // 로컬
	
	private static final boolean IS_DEV_MODE = true; // true = Local / false = Server
	
	public static final String SAVE_DIR;
	
	static {
		if (IS_DEV_MODE) {
			SAVE_DIR = "C:/BoardStorage/";
		} else {
			SAVE_DIR = "/home/choiy/public_html/BoardStorage/";
		}
		System.out.println("Storage Mode : " + (IS_DEV_MODE ? "Local" : "Server"));
		System.out.println("Storage Save : " + SAVE_DIR);
	}
	
	// 첨부파일 저장소 생성
	public void createStorageDirectory() {
		File dir = new File(SAVE_DIR);
		// 디텍토리 존재 여부 확인 (없으면 생성)
		if (!dir.exists()) {
			boolean isCreated = dir.mkdirs();
			if (isCreated) {
				System.out.println("[SUCCESS] File storage (folder) creation complete. : " + dir.getAbsolutePath());
			} else {
				System.err.println("[ERROR] Folder creation failed.");
			}
		} else {
			System.out.println("[INFO] The file repository already exists. : " + dir.getAbsolutePath());
		}
		// 권한 확인
		if (dir.exists() && !dir.canWrite()) {
			System.err.println("[WARING] You do not have write permission to the file in that folder.");
		}
	}
	
	// 첨부파일 저장
	public static String saveFile(File sourceFile) {
		if (sourceFile == null || !sourceFile.exists()) {
			return null;
		}
		String originName = sourceFile.getName();
		String uuid = UUID.randomUUID().toString();
		String saveName = uuid + "_" + originName;
		
		File destFile = new File(SAVE_DIR + saveName);
		try {
			Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File Save : " + destFile.getAbsolutePath());
			return saveName;
		} catch (IOException e) {
			System.err.println("File Save Fail : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	// 첨부파일 삭제
	public static boolean deleteFile(String saveName) {
		if (saveName == null || saveName.isEmpty()) return false;
		
		File file = new File(SAVE_DIR + saveName);
		if (file.exists()) {
			if (file.delete()) {
				System.out.println("File Delete : " + file.getAbsolutePath());
				return true;
			} else {
				System.err.println("File Delete Fail");
				return false;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		StorageSetup setup = new StorageSetup();
		setup.createStorageDirectory();
	}
}
