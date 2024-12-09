package com.example.powerpulse.ui.profile

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.powerpulse.activity.ChangePasswordActivity
import com.example.powerpulse.activity.PrivacyPolicyActivity
import com.example.powerpulse.activity.SignInActivity
import com.example.powerpulse.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var realtimeDB: DatabaseReference

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        realtimeDB = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        val signOutButton: CardView = binding.buttonSignOut
        val buttonPrivacyPolicy: CardView = binding.buttonPrivacyPolicy
        val buttonChangePassword: CardView = binding.buttonChangePassword

        buttonChangePassword.setOnClickListener {
            // Navigate to Change Password fragment or activity
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        buttonPrivacyPolicy.setOnClickListener {
            // Navigate to Privacy Policy fragment or activity
            val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        signOutButton.setOnClickListener {
            // Get an instance of FirebaseAuth
            val firebaseAuth = FirebaseAuth.getInstance()

            // Sign out the user
            firebaseAuth.signOut()

            // Redirect the user to the login screen or a similar screen
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
        }

        val currentUserUid = auth.uid.toString()

        // Update Role
        fetchUserRole(currentUserUid) { role ->
            if (role == "admin") {
                binding.textViewProfileRole.text = "Admin"
                binding.buttonViewUsers.visibility = View.VISIBLE
            } else {
                binding.textViewProfileRole.text = "User"
                binding.buttonViewUsers.visibility = View.GONE
            }
        }

        // Update Name
        fetchUserName(currentUserUid) { fullName ->
            binding.textViewProfileName.text = "Hi, $fullName!"
        }

        return root
    }

    private fun fetchUserRole(uid: String, callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val userRef = database.getReference("users/$uid/role")

        userRef.get().addOnSuccessListener { dataSnapshot ->
            val role = dataSnapshot.getValue(String::class.java)
            callback(role)
        }.addOnFailureListener {
            callback(null) // Handle errors gracefully
        }
    }

    private fun fetchUserName(uid: String, callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance("https://powerpulse-56790-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val userRef = database.getReference("users/$uid/fullName")

        userRef.get().addOnSuccessListener { dataSnapshot ->
            val fullName = dataSnapshot.getValue(String::class.java)
            callback(fullName)
        }.addOnFailureListener {
            callback(null) // Handle errors gracefully
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}