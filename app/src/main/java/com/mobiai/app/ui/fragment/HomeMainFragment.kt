package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobiai.R
import com.mobiai.app.api.ApiClient
import com.mobiai.app.model.TranslateRequest
import com.mobiai.app.model.TranslateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.mobiai.app.api.TranslateService

class HomeMainFragment : Fragment() {

    private lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.editTextText)

        editText.setOnEditorActionListener { _, actionId, _ ->
            val input = editText.text.toString()
            if (input.isNotEmpty()) {
                translateText(input)
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập từ!", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun translateText(text: String) {
        // Show loading indicator (optional: you can add a ProgressBar to the layout)
        // progressBar.visibility = View.VISIBLE

        val translateService = ApiClient.translateService
        val request = TranslateRequest(
            q = text,
            source = "en",  // Ngôn ngữ nguồn (tiếng Anh)
            target = "vi"   // Ngôn ngữ đích (tiếng Việt)
        )

        translateService.translateText(request).enqueue(object : Callback<TranslateResponse> {
            override fun onResponse(
                call: Call<TranslateResponse>,
                response: Response<TranslateResponse>
            ) {
                // Hide loading indicator (optional)
                // progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val translatedText = response.body()?.translatedText
                    Toast.makeText(requireContext(), translatedText ?: "Không có kết quả", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Lỗi API: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TranslateResponse>, t: Throwable) {
                // Hide loading indicator (optional)
                // progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        fun instance(): HomeMainFragment {
            return HomeMainFragment()
        }
    }
}
