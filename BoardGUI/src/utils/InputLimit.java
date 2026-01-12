package utils;

import javax.swing.*;
import javax.swing.text.*;

public class InputLimit {
	// 텍스트 필드의 최대 길이 제한 메서드
	public void checkMaxLength(JTextComponent textComponent, int maxLength) {
	    ((AbstractDocument) textComponent.getDocument()).setDocumentFilter(new DocumentFilter() {
	        @Override
	        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	            // 붙여넣기나 대량 입력 시 길이 체크
	            int currentLength = fb.getDocument().getLength();
	            int overLimit = (currentLength + text.length()) - length - maxLength;
	            
	            if (overLimit > 0) {
	                // 제한을 넘는 부분은 잘라내고 입력 (선택사항: 그냥 입력 막으려면 return)
	                text = text.substring(0, text.length() - overLimit);
	            }
	            
	            if (text.length() > 0) {
	                super.replace(fb, offset, length, text, attrs);
	            }
	        }
	        
	        @Override
	        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
	            // 일반 타이핑 시 길이 체크
	            int currentLength = fb.getDocument().getLength();
	            if (currentLength + string.length() > maxLength) {
	                return; // 최대 길이 초과 시 입력 무시
	            }
	            super.insertString(fb, offset, string, attr);
	        }
	    });
	}
	
	// 텍스트 필드 최소 길이 제한 메서드
	public static boolean checkMinLength(JTextComponent textComponent, int minLength, String fieldName) {
        String text = textComponent.getText().trim();
        if (text.length() < minLength) {
            JOptionPane.showMessageDialog(null, fieldName + "은(는) 최소 " + minLength + "자 이상이어야 합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            textComponent.requestFocus(); // 재입력 이동
            return false; // 검사 실패
        }
        return true; // 검사 통과
    }
}
