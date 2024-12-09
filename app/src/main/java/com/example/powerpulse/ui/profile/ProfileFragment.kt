package com.example.powerpulse.ui.profile

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

        // Load cached name
        val cachedName = getNameFromPreferences()
        if (cachedName != null) {
            binding.textViewProfileName.text = "Hi, $cachedName!"
        } else {
            // Fetch name from Firebase if not cached
            val userId = auth.currentUser?.uid
            if (userId != null) {
                realtimeDB.child("users").child(userId).child("fullName").get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val fullName = snapshot.value.toString()
                            binding.textViewProfileName.text = "Hi, $fullName!"
                            saveNameToPreferences(fullName) // Cache the name
                        }
                    }
                    .addOnFailureListener {
                        // Handle the error, e.g., show a message to the user
                        binding.textViewProfileName.text = "Hi, User!"
                    }
            }
        }

        return root
    }

    private fun saveNameToPreferences(name: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        editor.putString("profile_name", name)
        editor.apply()
    }

    private fun getNameFromPreferences(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPreferences.getString("profile_name", null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}