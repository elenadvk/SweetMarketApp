//package com.bitmobileedition.sweetmarket
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class AuthViewModel : ViewModel() {
//
//    private val _authResponse = MutableLiveData<AuthResponse>()
//    val authResponse: LiveData<AuthResponse> get() = _authResponse
//
//    private val _registerResponse = MutableLiveData<RegisterResponse>()
//    val registerResponse: LiveData<RegisterResponse> get() = _registerResponse
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> get() = _errorMessage
//
//    fun register(username: String, email: String, password: String) {
//        val registerRequest = RegisterRequest(username, email, password)
//        RetrofitInstance.instance.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
//                if (response.isSuccessful) {
//                    _registerResponse.postValue(response.body())
//                } else {
//                    _errorMessage.postValue("Registration failed!")
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                _errorMessage.postValue(t.message)
//            }
//        })
//    }
//
//    fun login(username: String, password: String) {
//        val authRequest = AuthRequest(username, password)
//        RetrofitInstance.instance.login(authRequest).enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                if (response.isSuccessful) {
//                    _authResponse.postValue(response.body())
//                } else {
//                    _errorMessage.postValue("Login failed!")
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                _errorMessage.postValue(t.message)
//            }
//        })
//    }
//}
