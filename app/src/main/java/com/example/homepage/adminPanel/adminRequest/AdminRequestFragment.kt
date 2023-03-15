package com.example.homepage.adminPanel.adminRequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homepage.R
import com.example.homepage.adminPanel.adminRequest.Adapter.AdminRequestAdapter
import com.example.homepage.adminPanel.adminRequest.Adapter.AdminRequestViewHolder
import com.example.homepage.adminPanel.adminRequest.Adapter.GenericAdapter
import com.example.homepage.adminPanel.adminRequest.Model.Admin
import com.example.homepage.adminPanel.adminRequest.Model.AdminRequestViewModel
import com.example.homepage.databinding.FragmentAdminRequestBinding


class AdminRequestFragment : Fragment() {


    private var fragmentBinding: FragmentAdminRequestBinding? = null
    private val viewBinding get() = fragmentBinding!!

    private val adminRequestRecyclerView: RecyclerView by lazy { viewBinding.recyclerView }

    private val viewModel by viewModels<AdminRequestViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentAdminRequestBinding.inflate(inflater, container, false)
        return viewBinding.root
    }


    val adapter = AdminRequestAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminRequestRecyclerView.layoutManager = LinearLayoutManager(context)
        adminRequestRecyclerView.setHasFixedSize(true)




        try {
            val genericAdapter = GenericAdapter<Admin, AdminRequestViewHolder>(
                AdminRequestViewHolder::class.java,
                R.layout.card_admin_request
            ) { holder, item ->
                holder.bind(item)
            }
            adminRequestRecyclerView.adapter = adapter
            viewModel.allAdminRequest.observe(viewLifecycleOwner) { adminRequest ->
                adapter.updateAdminRequestList(adminRequest)
                //genericAdapter.updateList(adminRequest)
            }

        } catch (exception: RuntimeException) {
            Toast.makeText(
                context,
                "An error occurred while creating the adapter!",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

}