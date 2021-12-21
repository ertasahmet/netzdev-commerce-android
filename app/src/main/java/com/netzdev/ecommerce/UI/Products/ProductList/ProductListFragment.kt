package com.netzdev.ecommerce.UI.Products.ProductList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.User.UserActivity
import com.netzdev.ecommerce.Utilities.GlobalApp
import kotlinx.android.synthetic.main.product_list_fragment.view.*

class ProductListFragment : Fragment() {

    lateinit var linearLayoutManager: LinearLayoutManager
    var productListRecyclerAdapter: ProductListRecyclerAdapter? = null

    var page: Int = 0
    var isLoading = false
    var isLastPage = false

    private lateinit var viewModel: ProductListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        var root = inflater.inflate(R.layout.product_list_fragment, container, false)

        UserActivity.setLoadingStatus(viewModel, viewLifecycleOwner)
        UserActivity.setErrorStatus(viewModel, viewLifecycleOwner)

        // Bu şekilde butona tıklandığında navigation dosyasında fargment'lara verdiğimz id'ler ile fragment'ları yer değiştrebiliyoruz. Arayüz kısmında ise navHostFragment kullanıyoruz.
        root.btnAddProductNav.setOnClickListener {
            it.findNavController().navigate(R.id.productAddFragmentNav)
        }

        linearLayoutManager = LinearLayoutManager(GlobalApp.getAppContext())
        root.rvProducts.layoutManager = linearLayoutManager

        if (page == 0) {
            viewModel.getProducts(page)
        }
        else {
            root.rvProducts.adapter = productListRecyclerAdapter
        }

        viewModel.products.observe(viewLifecycleOwner, {

            // Gelen veri seti boş ise ve aynı zamanda sayfa ilk sayfa da değilse bu demektir ki son sayfadayız ve başka veri yok.
            if (it.size == 0 && page != 0) {
                productListRecyclerAdapter?.removeLoading()
                isLoading = false
                isLastPage = true
            }

            else {

                if (page == 0) {

                    // Burada da recyclerView'a apply metodu ile girerek onun property'lerine direk yazarak ulaşabiliyoruz. Mesela rvProducts.adapter değil de apply parantezleri içinde direk adapter = diyerek atama yapabiliriz.
                    root.rvProducts.apply {

                        productListRecyclerAdapter = ProductListRecyclerAdapter(it) { product ->

                            // Item Click
                            var action = ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(product.Id)

                            var navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                            var navController = navHostFragment.navController

                            navController.navigate(action)

                        }

                        adapter = productListRecyclerAdapter
                    }
                }

                if (page != 0) {
                    productListRecyclerAdapter?.removeLoading()

                    isLoading = false

                    // Burada diyoruz ki recyclerView içerisinde şuan api'den çektiğimiz data var mı diye bakıyoruz. Eğer yok ise recyclerView içine ekliyoruz.
                    var isExist = productListRecyclerAdapter!!.products.contains(it[0])
                    if (!isExist) productListRecyclerAdapter!!.addProduct(it)
                }
            }

        })


        // Burada da recyclerView'ın scroll olma olayını kontrol ediyoruz. Scroll olduğunda ilgili metodu çalıştırıyoruz.
        root.rvProducts.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Önce ekranda gözükne item sayısını, toplam item sayısını ve o anda ilk gözüken item sayısını alıyoruz.
                var visibleItemCount = linearLayoutManager.childCount
                var totalItemCount = linearLayoutManager.itemCount
                var firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                // Bir yüklenme yok ise ve son sayfa değilse diyoruz.
                if (!isLoading && !isLastPage) {

                    Log.i("OkHttp", "$visibleItemCount + $firstVisibleItemPosition >= $totalItemCount")

                    // Burada bir hesaplama yapıyoruz. Nerdeyse son eleman da gözüktüyse artık diğer sayfa için istek atıcaz.
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount))
                    {
                        // Loading'i true yapıyoruz, loading metodunu çalıştırıyoruz ki progressBar gözüksün.
                        isLoading = true
                        productListRecyclerAdapter?.addLoading()

                        // Sonra sayfaya 5 ekliyoruz ki 5 - 5 getiriyor elemanları. Sonra da viewModel ile api'ye istek atıyoruz.
                        page += 5
                        viewModel.getProducts(page)
                    }
                } }
        })
        return root
    }

}