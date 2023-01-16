package com.example.homepage.plazaDashboard.Adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.homepage.R
import com.example.homepage.plaza.Model.Announcements
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class PlazaDashBoardAdapter(inflater: LayoutInflater) :
    RecyclerView.Adapter<PlazaDashBoardAdapter.PlazaDashBoardViewHolder>() {


    private val announcements = ArrayList<Announcements>()
    private var _inflater: LayoutInflater = inflater
    private lateinit var database: DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlazaDashBoardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_plaza_dashboard, parent, false)
        return PlazaDashBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlazaDashBoardViewHolder, position: Int) {
        val currentItem = announcements[position]
        val context = holder.itemView.context
        holder.announcersName.text = currentItem.sellersDetails?.split(" ")?.get(1)
        holder.announcementsTopic.text = currentItem.productAuthor
        holder.announcementDetails.text = currentItem.productDetails

        val auth = Firebase.auth
        val user = auth.currentUser!!.uid
        database = Firebase.database.reference
        val userProductReference =
            FirebaseDatabase.getInstance().getReference("user-posted-items").child(user)
        val productID = currentItem.productId.toString()

        val announcementsCategory = currentItem.productCategory
        val publicPostReferenceCategory = FirebaseDatabase.getInstance().getReference("public-announcements/$announcementsCategory")
        val publicPostReferenceAll = FirebaseDatabase.getInstance().getReference("public-announcements/all")

        val sellersContactNo = currentItem.sellersDetails?.split(" ")?.get(0)


        holder.deleteButton.setOnClickListener {
            userProductReference.child(productID).removeValue()
            publicPostReferenceCategory.child(productID).removeValue()
            publicPostReferenceAll.child(productID).removeValue()
        }

        holder.updateProductButton.setOnClickListener {
            val rootLayout = _inflater.inflate(R.layout.popup_announcement, null)
            val productName = rootLayout.findViewById<EditText>(R.id.productNamePop)
            val productAuthor = rootLayout.findViewById<EditText>(R.id.productAuthorPop)
            val productPrice = rootLayout.findViewById<EditText>(R.id.productPricePop)
            val sellersContactNo2 = rootLayout.findViewById<EditText>(R.id.sellerContactNoPop)
            val addButton = rootLayout.findViewById<Button>(R.id.AddButton)
            val closeButton = rootLayout.findViewById<Button>(R.id.CloseButton)
            val productDescription = rootLayout.findViewById<EditText>(R.id.productDetailsPop)
            val productCategory = rootLayout.findViewById<Spinner>(R.id.bookCategoryList)
            productName.setText(currentItem.productName.toString())
            productAuthor.setText(currentItem.productAuthor.toString())
            productPrice.setText(currentItem.productPrice.toString())
            productDescription.setText(currentItem.productDetails.toString())
            addButton.text = "UPDATE"
            sellersContactNo2.setText(sellersContactNo)


            val popupWindow = PopupWindow(
                rootLayout,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.update()
            popupWindow.elevation = 20.5F
            popupWindow.showAtLocation(

                holder.updateProductButton,
                Gravity.CENTER,
                0,
                -500
            )
            closeButton.setOnClickListener {
                popupWindow.dismiss()
            }

            addButton.setOnClickListener {
                if (productName.text.toString() == "")
                    Toast.makeText(context, "Please fill up all Product Name", Toast.LENGTH_SHORT)
                        .show()
                else if (productCategory.selectedItem.toString() == "Category")
                    Toast.makeText(context, "Please fill up Product Category", Toast.LENGTH_SHORT)
                        .show()
                else if (productPrice.text.toString() == "")
                    Toast.makeText(context, "Please fill up Product Price", Toast.LENGTH_SHORT)
                        .show()
                else {

                    if (sellersContactNo != null) {
                        updateMaterial(
                            user,
                            productName.text.toString(),
                            productAuthor.text.toString(),
                            productCategory.selectedItem.toString(),
                            productPrice.text.toString(),
                            sellersContactNo,
                            currentItem.sellersDetails.toString(),
                            currentItem.productId.toString(),
                            productDescription.text.toString()

                        )
                    }
                    popupWindow.dismiss()
                }
            }

        }


    }

    private fun updateMaterial(
        user: String,
        productName: String,
        productAuthor: String,
        productCategory: String,
        productPrice: String,
        sellersContactNo: String,
        sellersDetails: String,
        key: String,
        productDetailsWrite: String,

        ) {
        val updateMaterial = Announcements(
            user,
            productName,
            productAuthor,
            productCategory,
            productPrice,
            sellersContactNo,
            sellersDetails,
            key,
            productDetailsWrite
        )
        val taskValues = updateMaterial.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/user-posted-items/$user/$key" to taskValues,
            "/public-posts/$key" to taskValues
        )
        database.updateChildren(childUpdates)

    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    fun updatePlazaDashboardList(materials: List<Announcements>) {

        this.announcements.clear()
        this.announcements.addAll(materials)
        notifyDataSetChanged()

    }

    class PlazaDashBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val announcersName: TextView = itemView.findViewById(R.id.productNameCard)
        val announcementsTopic: TextView = itemView.findViewById(R.id.productAuthorNameCard)
        val announcementDetails: TextView= itemView.findViewById(R.id.announcementDetails)
        val deleteButton: Button = itemView.findViewById(R.id.deleteProductButton)
        val updateProductButton: Button = itemView.findViewById(R.id.updateProductButton)

    }
}