package com.example.musicapplicationse114.ui.screen.login

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapplicationse114.MainViewModel
import com.example.musicapplicationse114.auth.TokenManager
import com.example.musicapplicationse114.common.enum.LoadStatus
import com.example.musicapplicationse114.model.UserLoginRequest
import com.example.musicapplicationse114.repositories.Api
import com.example.musicapplicationse114.repositories.MainLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val successMessage : String = "",
    val errorMessage : String = "",
    var isShowPassword: Boolean = false,
    val status: LoadStatus = LoadStatus.Init()
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainLog: MainLog?,
    private val api: Api?,
    private val tokenManager: TokenManager?
): ViewModel() {

    val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)

    }

    fun updateSuccessMessage(successMessage: String){
        _uiState.value = _uiState.value.copy(successMessage = successMessage)
    }

    fun updateErrorMessage(errorMessage: String){
        _uiState.value = _uiState.value.copy(errorMessage = errorMessage)
    }

    fun reset()
    {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }

    fun changeIsShowPassword()
    {
        _uiState.value = _uiState.value.copy(isShowPassword = !_uiState.value.isShowPassword)
    }
//    fun login1()
//    {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
//            try {
//                val result = api?.login1(_uiState.value.username, _uiState.value.password)
//                _uiState.value = _uiState.value.copy(status = LoadStatus.Success())
//            }catch (ex: Exception){
//                _uiState.value = _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
//            }
//        }
//    }

    fun login(){
        viewModelScope.launch {
            try{
                _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
                val result = api?.login(UserLoginRequest(_uiState.value.username, _uiState.value.password))
                if(result != null && result.isSuccessful){
                    val accessToken = result.body()?.access_token
                    if(accessToken != null){
                        val bearer = "Bearer $accessToken"
                        tokenManager?.saveToken(bearer)
                        tokenManager?.saveUserName(getUserName())
                        val userId = tokenManager?.decodeUserIdFromToken(bearer)
                        if (userId != null) {
                            tokenManager?.saveUserId(userId)
                            Log.d("Login", "Decoded userId: $userId")
                        }

                        _uiState.value = _uiState.value.copy(status = LoadStatus.Success())
                        updateSuccessMessage("Đăng nhập thành công")
                    } else {
                        _uiState.value = _uiState.value.copy(status = LoadStatus.Error(result.body()?.message.toString()))
                        updateErrorMessage("Đăng nhập thất bại")
                        Log.e("SignUpError", "Response body: ${result.body()?.toString()}")
                        Log.e("SignUpError", "Response code: ${result.code()}")
                        Log.e("SignUpError", "AccessToken: ${result.body()?.access_token}")
                    }
                }
                else
                {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Error(result?.body()?.message.toString()))
                    updateErrorMessage("Sai toàn khoản hoặc mật khẩu vui lòng thử lại")
                }
            } catch (ex : Exception){
                _uiState.value = _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
                updateErrorMessage("Lỗi kết nối tới máy chủ")
            }
        }
    }


    fun getUserName() : String
    {
        return _uiState.value.username
    }
}
