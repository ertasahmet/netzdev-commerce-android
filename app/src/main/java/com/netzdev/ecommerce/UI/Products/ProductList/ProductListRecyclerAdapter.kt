package com.netzdev.ecommerce.UI.Products.ProductList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.R
import com.squareup.picasso.Picasso

// Burada diyoruz ki constructor'da productList al, ikinci parametre olarak da bir nesne tıklanma olayı için parametre olarak Product nesnesi alan ve geriye birşey dönmeyen fonksiyon al diyoruz. Bu adapter'ı çağırdığımızda oradaki metod çalışacak.
class ProductListRecyclerAdapter(
    var products: ArrayList<Product>, private val itemClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Burada 2 tane viewType tanımlıyoruz, api'ye istek atılırken loading gözükecek, yüklenince o kaybolacak normal veri gözükecek.
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1

    // Verilen id'ye göre viewType'ı belirliyoruz. 0 ise loading'tir.
    override fun getItemViewType(position: Int): Int {
        return if (products[position].Id == 0) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_NORMAL
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // Burada arayüzü aldık.
        var inflater = LayoutInflater.from(parent.context)

        // Gelen ViewType parametresi ile viewType'ı kontrol ediyoruz. Ona göre tasarım döneceğiz. Loading tasarımında sadece spinner var, diğerinde normal ürün tasarımı var.
        if (viewType == VIEW_TYPE_LOADING) {
            return HolderLoading(
                inflater.inflate(
                    R.layout.recyclerview_loading_item, parent, false
                )
            )
        } else {
            return HolderProduct(
                inflater.inflate(
                    R.layout.recyclerview_product_item, parent, false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Her veri geldiğinde ürünü alıyoruz.
        val product = products[position]

        // Gelen Holder nesnesi eğer Holder Product ise yani Holder Loading değilse durumunu kontrol ediyoruz burada.
        if (holder is HolderProduct) {

            // Ürünleri nesnelere yazdırıyoruz.
            holder.txtName.text = product.Name
            holder.txtPrice.text = product.Price.toString()
            holder.txtProductCategory.text = product.Category?.Name

            val product_photo_url = "${ApiConsts.photoBaseUrl}/${product.PhotoPath}"

            // Picasso ile fotoyu da yazdırıyoruz.
            Picasso.get().load(product_photo_url).placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(holder.imgProductImage)

            // Burada da parametre olarak aldığımız fonksiyonu buraya veriyoruz, direk onu çalıştırıyoru onclick olayında.
            holder.itemView.setOnClickListener {
                itemClick
            }
        }
    }

    // İtem sayısını alıyoruz.
    override fun getItemCount(): Int {
        return products.size
    }

    // Loading olayı ekle diyoruz. Burada id'si 0 olan product nesnesi ekliyoruz veri listesine. Yukarıda da bunu viewType ile yakalıyoruz.
    fun addLoading() {
        var loadingProduct = Product(0, "", 0.0, "", 0, "", 0, null)

        products.add(loadingProduct)
        notifyDataSetChanged()
    }

    // Burada en son loading eklemiştik. Yüklenme bitince son eklenen nesneyi yani loading'i siliyoruz ki gözükmesin.
    fun removeLoading() {
        var position = products.size - 1
        products.removeAt(position)
        notifyDataSetChanged()
    }

    // Sayfalamada apiden yeni gelen sayfayı da ürünler listesine ekliyoruz.
    fun addProduct(newProducts: ArrayList<Product>) {
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

}