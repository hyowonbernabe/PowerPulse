package com.example.powerpulse.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.powerpulse.activity.PrivacyPolicyActivity
import com.example.powerpulse.activity.SignInActivity
import com.example.powerpulse.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val switchDarkMode: Switch = binding.switchDarkMode
        val signOutButton: CardView = binding.buttonSignOut
        val buttonPrivacyPolicy: CardView = binding.buttonPrivacyPolicy

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

        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        switchDarkMode.isChecked = isDarkMode

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("DARK_MODE", isChecked)
            editor.apply()

            requireActivity().recreate() // Recreate the activity to apply the theme
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}