package com.netzdev.ecommerce.UI.Products.ProductList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.netzdev.ecommerce.R
import kotlinx.android.synthetic.main.recyclerview_product_item.view.*

class HolderProduct (view: View) : RecyclerView.ViewHolder(view) {

    var txtName = view.findViewById<TextView>(R.id.txtRecyclerViewProductName)
    var txtPrice = view.findViewById<TextView>(R.id.txtRecyclerViewProductPrice)
    var txtProductCategory = view.findViewById<TextView>(R.id.txtRecyclerViewProductCategory)
    var imgProductImage = view.findViewById<ImageView>(R.id.imgRecyclerViewProductPhoto)

}