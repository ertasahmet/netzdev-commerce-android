package com.netzdev.ecommerce.Models

data class ApiResponse<T> (var isSuccessful:Boolean, var success:T? = null, var failure:ApiError? = null) {}