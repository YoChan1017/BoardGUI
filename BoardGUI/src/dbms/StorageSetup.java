package dbms;

import java.io.File;

public class StorageSetup {
	
	// 파일 저장 경로
	// Windows: "C:/BoardStorage/"
	// Linux:   "/home/사용자명/BoardStorage/"
	// Mac:     "/Users/사용자명/BoardStorage/"
	public static final String SAVE_DIR = "C:/BoardStorage/";
	
	// 파일 저장소 생성
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
	
	public static void main(String[] args) {
		StorageSetup setup = new StorageSetup();
		setup.createStorageDirectory();
	}
}
