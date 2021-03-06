package com.netzdev.ecommerce.UI.Products.ProductUpdate

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.Category
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.Products.ProductAdd.ProductAddFragment
import com.netzdev.ecommerce.UI.Products.ProductDetail.ProductDetailFragmentArgs
import com.netzdev.ecommerce.UI.User.UserActivity
import com.netzdev.ecommerce.Utilities.GlobalApp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_add_fragment.*
import kotlinx.android.synthetic.main.product_add_fragment.view.*
import kotlinx.android.synthetic.main.product_update_fragment.view.*
import kotlinx.android.synthetic.main.product_update_fragment.view.imgPickProductPhoto
import kotlinx.android.synthetic.main.product_update_fragment.view.spinnerAddFragmentCategories
import kotlinx.android.synthetic.main.product_update_fragment.view.txtAddFragmentProductColor
import kotlinx.android.synthetic.main.product_update_fragment.view.txtAddFragmentProductName
import kotlinx.android.synthetic.main.product_update_fragment.view.txtAddFragmentProductPrice
import kotlinx.android.synthetic.main.product_update_fragment.view.txtAddFragmentProductStock
import java.util.*

class ProductUpdateFragment : Fragment() {
    val arg : ProductUpdateFragmentArgs by navArgs()

    companion object {
        fun newInstance() = ProductUpdateFragment()
    }

    private lateinit var viewModel: ProductUpdateViewModel
    private lateinit var root: View
    private var fileUri : Uri? = null

    // Burada gallery'i g??steriyoruz ve foto??raf se??ece??imizi s??yl??yoruz. Manifest dosyas??nda da READ_EXTERNAL_STORAGE izni al??yoruz ki galeriyi okuyabilelim.
    private fun showGallery() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            startActivityForResult(it, ProductAddFragment.REQUEST_CODE_PICK_IMAGE)
        }
    }

    // Bu metodu override ediyoruz. Gallery activity'si sonu??land??????nda a????lacak.
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Burada da request'in bizim request'imiz olup olmad??????n?? ve sonucun da ok olup olmad??????n?? kontrol ediyoruz. e??er ikisi de okey ise foto se??ilmi??tir ve fotoyu al??p imageView'da g??steriyoruz.
        if (requestCode == ProductAddFragment.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            fileUri = intent!!.data!!
            root.imgPickProductPhoto.setImageURI(fileUri)
        }
    }

    // Burada da izin olmay??p izin istedi??imizde kullan??c??n??n verdi??i cevaba g??re buras?? ??al??????yor. Metodu override ettik.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // ??lgili request code bizim requestCode'umuz ise diyoruz.
        when (requestCode) {
            ProductAddFragment.REQUEST_CODE_PERMISSION -> {

                // Gelen grantResult bo?? de??ilse yani bir se??im var ise ve izin verilmi?? ise galeriyi g??ster diyoruz.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showGallery()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProductUpdateViewModel::class.java)
        root = inflater.inflate(R.layout.product_update_fragment, container, false)

        UserActivity.setLoadingStatus(viewModel, viewLifecycleOwner)
        UserActivity.setErrorStatus(viewModel, viewLifecycleOwner)

        var photoUrl = "${ApiConsts.photoBaseUrl}/${arg.product.PhotoPath}"

        Picasso.get().load(photoUrl).placeholder(R.drawable.no_image)
            .error(R.drawable.no_image).into(root.imgPickProductPhoto)

        root.txtAddFragmentProductName.editText?.setText(arg.product.Name)
        root.txtAddFragmentProductColor.editText?.setText(arg.product.Color)
        root.txtAddFragmentProductStock.editText?.setText(arg.product.Stock.toString())
        root.txtAddFragmentProductPrice.editText?.setText(arg.product.Price.toString())

        viewModel.getCategories().observe(viewLifecycleOwner, {

            // Burada spinner i??in bir adapter olu??turuyoruz.
            ArrayAdapter<Category>(GlobalApp.getAppContext(), android.R.layout.simple_spinner_item, it).also { categoryAdapter ->

                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                root.spinnerAddFragmentCategories.adapter = categoryAdapter

                // ??lgili kategoriyi se??ili hale getirmek i??in t??m kategori listesinden id'si bizim product'??n id'si olan??n position'??n?? al??yoruz ve select diyoruz.
                var selectedPosition = it.map {it.Id}.indexOf(arg.product.CategoryId)
                root.spinnerAddFragmentCategories.setSelection(selectedPosition)

            }
        })

        root.btnAddFragmentUpdateProduct.setOnClickListener {

            var productName = root.txtAddFragmentProductName.editText?.text.toString()
            var productPrice = root.txtAddFragmentProductPrice.editText?.text.toString()
            var productStock = root.txtAddFragmentProductStock.editText?.text.toString()
            var productColor = root.txtAddFragmentProductColor.editText?.text.toString()

            // Spinner'dan se??ili kategoriyi category modeli olarak ald??k.
            var category = spinnerAddFragmentCategories.selectedItem as Category

            // Product nesnei olu??turduk.
            var updateProduct = arg.product

            updateProduct.Name = productName
            updateProduct.Price = productPrice.toDouble()
            updateProduct.Stock = productStock.toInt()
            updateProduct.Color = productColor
            updateProduct.CategoryId = category.Id

            viewModel.updateProduct(updateProduct, fileUri).observe(viewLifecycleOwner, {

                if (it) {
                    findNavController().navigate(R.id.productListFragmentNav)
                }
            })

        }

        return root
    }


}