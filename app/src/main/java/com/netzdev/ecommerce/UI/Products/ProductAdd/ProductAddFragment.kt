package com.netzdev.ecommerce.UI.Products.ProductAdd

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.netzdev.ecommerce.Models.Category
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.User.UserActivity
import com.netzdev.ecommerce.Utilities.GlobalApp
import kotlinx.android.synthetic.main.product_add_fragment.*
import kotlinx.android.synthetic.main.product_add_fragment.view.*
import kotlinx.android.synthetic.main.signin_fragment.view.*

class ProductAddFragment : Fragment() {

    companion object {
        fun newInstance() = ProductAddFragment()

        const val REQUEST_CODE_PICK_IMAGE = 101
        const val REQUEST_CODE_PERMISSION = 200
    }

    private lateinit var viewModel: ProductAddViewModel
    private lateinit var root: View
    private var fileUri : Uri? = null

    // Burada gallery'i gösteriyoruz ve fotoğraf seçeceğimizi söylüyoruz. Manifest dosyasında da READ_EXTERNAL_STORAGE izni alıyoruz ki galeriyi okuyabilelim.
    private fun showGallery() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    // Bu metodu override ediyoruz. Gallery activity'si sonuçlandığında açılacak.
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Burada da request'in bizim request'imiz olup olmadığını ve sonucun da ok olup olmadığını kontrol ediyoruz. eğer ikisi de okey ise foto seçilmiştir ve fotoyu alıp imageView'da gösteriyoruz.
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            fileUri = intent!!.data!!
            root.imgPickProductPhoto.setImageURI(fileUri)
        }
    }

    // Burada da izin olmayıp izin istediğimizde kullanıcının verdiği cevaba göre burası çalışıyor. Metodu override ettik.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // İlgili request code bizim requestCode'umuz ise diyoruz.
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {

                // Gelen grantResult boş değilse yani bir seçim var ise ve izin verilmiş ise galeriyi göster diyoruz.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showGallery()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProductAddViewModel::class.java)
        root = inflater.inflate(R.layout.product_add_fragment, container, false)

        // Burada da userActivity'deki metodları set ettik ve böyle kolay şekilde loadingleri ve error'ları ayarlamış olduk.
        UserActivity.setLoadingStatus(viewModel, viewLifecycleOwner)
        UserActivity.setErrorStatus(viewModel, viewLifecycleOwner)


        // Burada kategorileri getiren metoda observe olduk ve ilgili category listesi datası set edildiğinde burası çalışacak.
        viewModel.getCategories().observe(viewLifecycleOwner, {

            // Yeni bir adapter oluşturduk, contexti ve tasarımı ve listeyi verdik. Buradaki tasarım normal sayfadaki spinnerin gözüken tasarımı. Alttaki tasarım ise spinner'a bir item seçmek için tıkladığımızda çıkacak olan tasarımdır. Also ile bu adapter ile yine işlem yapacağımızı söylüyoruz. Also'nun manası bu array adapter'dan değişken oluşturup ona yine değerler set etmek gibidir, aynı işlemi görür.
            ArrayAdapter<Category>(
                GlobalApp.getAppContext(), android.R.layout.simple_spinner_item, it
            ).also { categoryAdapter ->

                // Tıklandığında açılacak spinner tasarımını da gösterdik.
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Adapter'ı spinner'a set ettik.
                root.spinnerAddFragmentCategories.adapter = categoryAdapter
            }
        })

        // Kaydet butonuna tıklandığında verileri aldık.
        root.btnAddFragmentSaveProduct.setOnClickListener {
            var productName = root.txtAddFragmentProductName.editText?.text.toString()
            var productPrice = root.txtAddFragmentProductPrice.editText?.text.toString()
            var productStock = root.txtAddFragmentProductStock.editText?.text.toString()
            var productColor = root.txtAddFragmentProductColor.editText?.text.toString()

            // Spinner'dan seçili kategoriyi category modeli olarak aldık.
            var category = spinnerAddFragmentCategories.selectedItem as Category

            // PRoduct nesnei oluşturduk.
            var product = Product(
                0,
                productName,
                productPrice.toDouble(),
                productColor,
                productStock.toInt(),
                "",
                category.Id,
                null
            )

            // Api'ye istek attık ve sonucu bekledik.
            viewModel.addProduct(product, fileUri).observe(viewLifecycleOwner, {

                if (it != null) {
                    Toast.makeText(activity, "Ürün kaydedildi", Toast.LENGTH_LONG)

                    root.findNavController().navigate(R.id.productListFragmentNav)
                }
            })
        }

        root.imgPickProductPhoto.setOnClickListener {

            // Burada izini kontrol ediyoruz. İzin var ise galeriyi açıyoruz, izin yok ise izin istiyoruz.
            if (ContextCompat.checkSelfPermission(
                    GlobalApp.getAppContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION
                )
            } else {
                showGallery()
            }
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}