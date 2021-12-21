package com.netzdev.ecommerce.UI.Products.ProductDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.User.UserActivity
import com.netzdev.ecommerce.Utilities.GlobalApp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_detail_fragment.view.*

class ProductDetailFragment : Fragment() {

    // navArgs metodundan navigation'daki FragmentArgs'lara erişiyoruz. Bu sayede buraya gelen veriye ulaşabileceğiz.
    val arg : ProductDetailFragmentArgs by navArgs()

    companion object {
        fun newInstance() = ProductDetailFragment()
    }

    // ViewModel'i tanımladık.
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        var root = inflater.inflate(R.layout.product_detail_fragment, container, false)

        // Activity'den loading ve error durumunu handle ettik.
        UserActivity.setLoadingStatus(viewModel, viewLifecycleOwner)
        UserActivity.setErrorStatus(viewModel, viewLifecycleOwner)

        // Burada isimsiz ve parametre olarak product alan ve birşey dönmeyen bir metod tanımladık.
        var p: (Product?) -> Unit = {

            if (it != null) {

                // Gelen product null değilse arayüzdeki itemlere ilgili değerleri atadık.
                product = it!!
                root.txtProductName.text = it.Name
                root.txtProductColor.text = it.Color
                root.txtProductPrice.text = it.Price.toString()
                root.txtProductStock.text = it.Stock.toString()
                root.txtProductCategory.text = it.Category?.Name

                var fullPhotoUrl = "${ApiConsts.photoBaseUrl}/${it.PhotoPath}"

                Picasso.get().load(fullPhotoUrl).placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image).into(root.imgProductImage)
            }
        }

        // ViewModel'de getProduct metodunu bize gelen id ile çağırıyoruz ki ilgili product'ın verilerini çeksin. Observe ile veri geldiğinde yukarıda p diye tanımladığımız metodu çağırıyoruz.
        viewModel.getProduct(arg.productId).observe(viewLifecycleOwner, p)

        // Ürün silme durumunda viewModel'de silme eventine observe olduk, silince bilgi veriyoruz ve geri ürünlerin listelendiği sayfaya gidiyoruz.
        root.btnProductDelete.setOnClickListener {
            viewModel.deleteProduct(arg.productId).observe(viewLifecycleOwner, {

                if (it) {
                    Snackbar.make(root, "Id'si ${arg.productId} olan ürün silinmiştir.", Snackbar.LENGTH_LONG).show()

                    root.findNavController().navigate(R.id.productListFragmentNav)
                }
            })
        }

        // Update butonuna basınca da update fragment'ına gidiyoruz.
        root.btnProductUpdate.setOnClickListener {

            var action = ProductDetailFragmentDirections.actionProductDetailFragmentToProductUpdateFragment(product)
            root.findNavController().navigate(action)

        }

        return root
    }


}