document.addEventListener('DOMContentLoaded', function() {
  const form = document.querySelector('.form-signup');
  const passwordInput = document.getElementById('floatingPassword');
  const passwordConfirmInput = document.getElementById('floatingPasswordConfirm');
  const emailInput = document.getElementById('floatingEmail');
  const nickNameInput = document.getElementById('nickname');

  // 비밀번호 유효성 검사 함수
  function validatePassword(password) {
    const minLength = 8;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (password.length < minLength) {
      return '비밀번호는 최소 8자 이상이어야 합니다.';
    }
    if (!hasUpperCase) {
      return '비밀번호에는 최소 한 개의 대문자가 포함되어야 합니다.';
    }
    if (!hasLowerCase) {
      return '비밀번호에는 최소 한 개의 소문자가 포함되어야 합니다.';
    }
    if (!hasNumber) {
      return '비밀번호에는 최소 한 개의 숫자가 포함되어야 합니다.';
    }
    if (!hasSpecialChar) {
      return '비밀번호에는 최소 한 개의 특수문자가 포함되어야 합니다.';
    }
    return '';
  }

  // 에러 메시지 표시 함수
  function showError(inputElement, message) {
    const formFloating = inputElement.closest('.form-floating') || inputElement.parentNode;
    let errorElement = formFloating.querySelector('.invalid-feedback');
    if (!errorElement) {
      errorElement = document.createElement('div');
      errorElement.className = 'invalid-feedback';
      formFloating.appendChild(errorElement);
    }
    errorElement.textContent = message;
    inputElement.classList.add('is-invalid');
  }

  // 이전 에러 메시지 제거 함수
  function clearErrors() {
    const errorElements = form.querySelectorAll('.invalid-feedback');
    errorElements.forEach(element => element.remove());
    [passwordInput, passwordConfirmInput, emailInput, nickNameInput].forEach(input => input.classList.remove('is-invalid'));
  }

  // 폼 검증 및 제출 함수
  window.handleFormSubmit = function(event) {
    console.log('폼 제출 시도...');

    const password = passwordInput.value;
    const passwordConfirm = passwordConfirmInput.value;
    const email = emailInput.value;
    const nickName = nickNameInput.value;

    // 입력 데이터 로그
    console.log('입력 데이터:', { email, password, passwordConfirm, nickName });

    // 이전 에러 메시지 초기화
    clearErrors();

    // 기본 입력 검증 (빈 값 등)
    if (!email || !password || !passwordConfirm || !nickName) {
      showError(submitButton || form, '모든 필드를 입력해주세요.');
      console.log('입력 누락');
      return false;
    }

    // 비밀번호 강도 검증
    const passwordError = validatePassword(password);
    if (passwordError) {
      showError(passwordInput, passwordError);
      console.log('비밀번호 검증 실패:', passwordError);
      return false;
    }

    // 비밀번호 일치 여부 확인
    if (password !== passwordConfirm) {
      showError(passwordConfirmInput, '비밀번호가 일치하지 않습니다.');
      console.log('비밀번호 불일치');
      return false;
    }

    // 모든 검증 통과 시
    console.log('모든 검증 통과, 폼 제출 허용...');
    return true; // 기본 폼 제출 허용 (preventDefault() 제거로 기본 동작)
  };

  // 엔터 키 처리 (기본 제출에 의존)
  [passwordInput, passwordConfirmInput, emailInput, nickNameInput].forEach(input => {
    input.addEventListener('keypress', function(event) {
      if (event.key === 'Enter') {
        console.log('엔터 키 입력 감지');
        if (!window.handleFormSubmit(event)) {
          return false // 검증 실패 시만 제출 막기
        }
        // 통과 시 기본 제출 허용
      }
    });
  });

  // 실시간 유효성 검사
  passwordInput.addEventListener('input', function() {
    clearErrors();
    const error = validatePassword(passwordInput.value);
    if (error) {
      showError(passwordInput, error);
    }
  });

  passwordConfirmInput.addEventListener('input', function() {
    clearErrors();
    if (passwordInput.value !== passwordConfirmInput.value) {
      showError(passwordConfirmInput, '비밀번호가 일치하지 않습니다.');
    }
  });
});