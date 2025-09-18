package com.example.aispeakingbuddy.signupage

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.aispeakingbuddy.R
import com.example.aispeakingbuddy.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)

        setupUI()
        setupClickListeners()
        loadPrivacyPolicy()
    }

    private fun setupUI() {
        window.statusBarColor = getColor(R.color.primary_color)

        // Setup WebView
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                binding.progressBar.visibility = android.view.View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAccept.setOnClickListener {
            // Mark as accepted and return
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun loadPrivacyPolicy() {
        // You can load from URL or local HTML
        val privacyPolicyHtml = createPrivacyPolicyHtml()
        binding.webView.loadDataWithBaseURL(null, privacyPolicyHtml, "text/html", "UTF-8", null)
    }

    private fun createPrivacyPolicyHtml(): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Privacy Policy</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        margin: 0;
                        padding: 20px;
                        color: #333;
                        background-color: #f8f9fa;
                    }
                    .container {
                        max-width: 800px;
                        margin: 0 auto;
                        background: white;
                        padding: 30px;
                        border-radius: 12px;
                        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                    }
                    h1 {
                        color: #667eea;
                        border-bottom: 3px solid #667eea;
                        padding-bottom: 10px;
                    }
                    h2 {
                        color: #764ba2;
                        margin-top: 30px;
                    }
                    .highlight {
                        background: linear-gradient(135deg, #667eea, #764ba2);
                        color: white;
                        padding: 15px;
                        border-radius: 8px;
                        margin: 20px 0;
                    }
                    .update-date {
                        background: #e8f4f8;
                        padding: 10px;
                        border-radius: 6px;
                        font-size: 14px;
                        color: #666;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🔒 Chính Sách Bảo Mật</h1>
                    
                    <div class="update-date">
                        <strong>Cập nhật lần cuối:</strong> ${getCurrentDate()}
                    </div>
                    
                    <div class="highlight">
                        <h3>🤖 AI Speaking Buddy cam kết bảo vệ quyền riêng tư của bạn</h3>
                        <p>Chúng tôi thu thập và sử dụng thông tin của bạn một cách minh bạch, an toàn và có trách nhiệm.</p>
                    </div>

                    <h2>1. 📊 Thông Tin Chúng Tôi Thu Thập</h2>
                    <ul>
                        <li><strong>Thông tin cá nhân:</strong> Tên, email, ảnh đại diện</li>
                        <li><strong>Dữ liệu học tập:</strong> Tiến độ học, điểm số, thời gian học</li>
                        <li><strong>Dữ liệu giọng nói:</strong> Ghi âm để cải thiện phát âm (chỉ khi bạn đồng ý)</li>
                        <li><strong>Thông tin thiết bị:</strong> Loại thiết bị, hệ điều hành, IP address</li>
                    </ul>

                    <h2>2. 🎯 Mục Đích Sử Dụng</h2>
                    <ul>
                        <li>Cung cấp dịch vụ học tiếng Anh cá nhân hóa</li>
                        <li>Theo dõi và cải thiện tiến độ học tập của bạn</li>
                        <li>Gửi thông báo và cập nhật về khóa học</li>
                        <li>Cải thiện chất lượng ứng dụng và dịch vụ</li>
                        <li>Hỗ trợ khách hàng và xử lý khiếu nại</li>
                    </ul>

                    <h2>3. 🔐 Bảo Mật Thông Tin</h2>
                    <ul>
                        <li>Mã hóa dữ liệu bằng SSL/TLS</li>
                        <li>Lưu trữ trên máy chủ an toàn với nhiều lớp bảo vệ</li>
                        <li>Chỉ nhân viên được ủy quyền mới có thể truy cập</li>
                        <li>Sao lưu dữ liệu thường xuyên</li>
                        <li>Tuân thủ các tiêu chuẩn bảo mật quốc tế</li>
                    </ul>

                    <h2>4. 🤝 Chia Sẻ Thông Tin</h2>
                    <p><strong>Chúng tôi KHÔNG bán thông tin cá nhân của bạn.</strong></p>
                    <p>Thông tin có thể được chia sẻ trong các trường hợp sau:</p>
                    <ul>
                        <li>Với sự đồng ý rõ ràng của bạn</li>
                        <li>Với các đối tác công nghệ để cung cấp dịch vụ (OpenAI, Google Speech API)</li>
                        <li>Khi pháp luật yêu cầu</li>
                        <li>Để bảo vệ quyền lợi hợp pháp của chúng tôi</li>
                    </ul>

                    <h2>5. 🍪 Cookies và Công Nghệ Theo Dõi</h2>
                    <ul>
                        <li>Sử dụng cookies để ghi nhớ đăng nhập và tùy chọn</li>
                        <li>Thu thập dữ liệu phân tích để cải thiện ứng dụng</li>
                        <li>Bạn có thể tắt cookies trong cài đặt trình duyệt</li>
                        <li>Một số tính năng có thể bị hạn chế khi tắt cookies</li>
                    </ul>

                    <h2>6. 👨‍👩‍👧‍👦 Quyền Riêng Tư Trẻ Em</h2>
                    <p>Chúng tôi không cố ý thu thập thông tin từ trẻ em dưới 13 tuổi. Nếu phát hiện, chúng tôi sẽ xóa ngay lập tức.</p>

                    <h2>7. 🌍 Chuyển Giao Dữ Liệu Quốc Tế</h2>
                    <p>Dữ liệu của bạn có thể được xử lý ở các quốc gia khác. Chúng tôi đảm bảo mức độ bảo vệ tương đương.</p>

                    <h2>8. ⏰ Lưu Trữ Dữ Liệu</h2>
                    <ul>
                        <li>Dữ liệu tài khoản: Cho đến khi bạn yêu cầu xóa</li>
                        <li>Dữ liệu học tập: 5 năm sau lần sử dụng cuối</li>
                        <li>Dữ liệu giọng nói: 1 năm (có thể xóa sớm hơn theo yêu cầu)</li>
                    </ul>

                    <h2>9. 🎛️ Quyền Của Bạn</h2>
                    <ul>
                        <li><strong>Truy cập:</strong> Xem thông tin chúng tôi có về bạn</li>
                        <li><strong>Sửa đổi:</strong> Cập nhật thông tin không chính xác</li>
                        <li><strong>Xóa:</strong> Yêu cầu xóa dữ liệu cá nhân</li>
                        <li><strong>Di chuyển:</strong> Xuất dữ liệu sang định dạng khác</li>
                        <li><strong>Từ chối:</strong> Không đồng ý xử lý dữ liệu</li>
                    </ul>

                    <h2>10. 📞 Liên Hệ</h2>
                    <p>Nếu có thắc mắc về chính sách này:</p>
                    <ul>
                        <li><strong>Email:</strong> privacy@aispeakingbuddy.com</li>
                        <li><strong>Điện thoại:</strong> 1900-xxx-xxx</li>
                        <li><strong>Địa chỉ:</strong> Hà Nội, Việt Nam</li>
                    </ul>

                    <div class="highlight">
                        <h3>🔄 Cập Nhật Chính Sách</h3>
                        <p>Chúng tôi có thể cập nhật chính sách này. Các thay đổi quan trọng sẽ được thông báo qua email hoặc thông báo trong ứng dụng.</p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}