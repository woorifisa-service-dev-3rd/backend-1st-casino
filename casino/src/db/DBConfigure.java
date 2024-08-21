//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * DB 설정 파일을 읽어서 properties 객체로 바인딩해주는 유틸 클래스
// */
//
//public class DBConfigure {
//	public static Properties readProperties() {
//
//		String propertiesFilePath = argument;
//		Properties properties = new Properties();
//
//		try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
//			properties.load(fis);
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}
//
//		return properties;
//	}
//
//}
