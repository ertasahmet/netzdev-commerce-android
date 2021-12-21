package com.netzdev.ecommerce.Models

// Burada class'ın parantez içine direk değişkenleri verirsek teker teker property ve getter setter'lara gerek kalmıyor.
// Class'ın başına eklediğimiz data keywordu sayesinde parantez içindeki değerlerin property olduğunu anlayıp getter setter olayını hallediyor.
data class UserSignUp (var UserName:String, var Email:String, var Password:String, var City:String) {
}