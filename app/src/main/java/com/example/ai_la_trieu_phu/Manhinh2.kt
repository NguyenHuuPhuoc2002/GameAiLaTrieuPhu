package com.example.ai_la_trieu_phu

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class Manhinh2 : AppCompatActivity(), View.OnClickListener {
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var dialog:AlertDialog
    private lateinit var tvQuestion: TextView
    private lateinit var tvContentQuestion: TextView
    private lateinit var tvAnswer1: TextView
    private lateinit var tvAnswer2: TextView
    private lateinit var tvAnswer3: TextView
    private lateinit var tvAnswer4: TextView
    private lateinit var mListQuestion: List<Question>
    private lateinit var mQuestion:Question
    private var help_half = false
    private var help_audience = false
    private var help_counsel = false
    private var help_next = false
    private var isPlayedSound = false
    private var previousMoney: String? = "0"
    private var isAnswerSelected = false
    private var timeLeftInMillis: Long = 30000
    private var currentQuestion:Int = 0
    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        startCountdown()
        mListQuestion = getListQuestion()
        //Nếu mListQuestion rỗng -> return
        if(mListQuestion.isEmpty()){
            return
        }
        // còn không thì set dữ liệu
        setDataQuestion(mListQuestion.get(currentQuestion))
    /*------------------------- Xử lí sự kiện ----------------------------------*/
        //Quyền trợ giúp hỏi ý kiến khán giả trong trường quay khi clik vào img button
        ask_audiencebtn()
        //Quyền trợ giúp bỏ 2 đáp án sai khi clik vào img button
        findViewById<ImageButton>(R.id.img5050).setOnClickListener {
            findViewById<ImageButton>(R.id.img5050).setImageResource(R.drawable.half5050_useless)
            if (!help_half) {
                // Hiển thị quyền trợ giúp lần đầu tiên
                half50_50()
                help_half = true
                // Vô hiệu hóa nút trợ giúp sau khi đã sử dụng lần đầu tiên
                findViewById<ImageButton>(R.id.img5050).isEnabled = false
            }
            Handler().postDelayed({
                mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.hal5050)
                playSound()
            }, 700)
        }
        //Quyền trợ giúp hỏi tổ tư vấn tại chỗ clik vào img button
        findViewById<ImageButton>(R.id.imgcounsel).setOnClickListener {
            findViewById<ImageButton>(R.id.imgcounsel).setImageResource(R.drawable.counsel_useless)
            if (!help_counsel) {
                // Hiển thị quyền trợ giúp lần đầu tiên
                counsel()
                help_counsel = true
                // Vô hiệu hóa nút trợ giúp sau khi đã sử dụng lần đầu tiên
                findViewById<ImageButton>(R.id.imgcounsel).isEnabled = false
            }
            Handler().postDelayed({
                mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.hal5050)
                playSound()
            }, 700)
            Handler().postDelayed({
                mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.hal5050)
                playSound()
            }, 1700)
            Handler().postDelayed({
                mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.hal5050)
                playSound()
            }, 2600)
        }
        //Quyền trợ giúp chuyển sang câu hỏi tiếp theo khi clik vào img button
        findViewById<ImageButton>(R.id.imgnext).setOnClickListener {
            findViewById<ImageButton>(R.id.imgnext).setImageResource(R.drawable.next_useless)
            if (!help_next) {
                // Hiển thị quyền trợ giúp lần đầu tiên
                help_next = true
                // Vô hiệu hóa nút trợ giúp sau khi đã sử dụng lần đầu tiên
                findViewById<ImageButton>(R.id.imgnext).isEnabled = false
                nextQuestion_helpNext()
            }
            Handler().postDelayed({
                mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.hal5050)
                playSound()
            }, 800)
        }
    }
    //Hàm xử lý quền trợ giúp hỏi ý kiến khán giả trong trường quay khi click vào button
    private fun ask_audiencebtn(){
        findViewById<ImageButton>(R.id.imgaskaudience).setOnClickListener {
            findViewById<ImageButton>(R.id.imgaskaudience).setImageResource(R.drawable.ask_audience_useless)
            if (!help_audience) {
                // Hiển thị quyền trợ giúp lần đầu tiên
                if(mQuestion.number == 1 || mQuestion.number == 5 || mQuestion.number == 8 || mQuestion.number == 13){
                    askAudien(50, 20, 25, 5)
                }else if(mQuestion.number == 2 || mQuestion.number == 6 || mQuestion.number == 9 || mQuestion.number == 15){
                    askAudien(20, 33, 37, 10)
                }else if (mQuestion.number == 7 || mQuestion.number == 10 || mQuestion.number == 14){
                    askAudien(5, 45, 5, 45)
                }else{
                    askAudien(26, 30, 24, 20)
                }
                help_audience = true
                // Vô hiệu hóa nút trợ giúp sau khi đã sử dụng lần đầu tiên
                findViewById<ImageButton>(R.id.imgaskaudience).isEnabled = false
            }
            mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.hal5050)
            playSound()
        }
    }
    //Hàm xử lí quyền trợ giúp hỏi tổ tư vấn tại chổ
    @SuppressLint("SetTextI18n")
    private fun counsel() {
        val buid = AlertDialog.Builder(this,R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.counsel_dialog, null)
        buid.setView(view)
        buid.setCancelable(false)
        dialog = buid.create()
        dialog.setCancelable(false)
        dialog.show()

        val txtcounsel1: TextView? = dialog.findViewById(R.id.txtcounsel1)
        val txtcounsel2: TextView? = dialog.findViewById(R.id.txtcounsel2)
        val txtcounsel3: TextView? = dialog.findViewById(R.id.txtcounsel3)
        if(mQuestion.number == 1 || mQuestion.number == 5 || mQuestion.number == 8 || mQuestion.number == 13)
        {
            Handler().postDelayed({
                txtcounsel1?.text = "Tôi Chọn Đáp Án  A"
            }, 800)
            Handler().postDelayed({
                txtcounsel2?.text = "Tôi Chọn Đáp Án  A"
            }, 1700)
            Handler().postDelayed({
                txtcounsel3?.text = "Tôi Chọn Đáp Án  C"
            }, 2600)
        }else if(mQuestion.number == 2 || mQuestion.number == 6 || mQuestion.number == 9){
            Handler().postDelayed({
                txtcounsel1?.text = "Tôi Chọn Đáp Án  C"
            }, 800)
            Handler().postDelayed({
                txtcounsel2?.text = "Tôi Chọn Đáp Án  C"
            }, 1700)
            Handler().postDelayed({
                txtcounsel3?.text = "Tôi Chọn Đáp Án  C"
            }, 2600)
        }else if (mQuestion.number == 7 || mQuestion.number == 10 || mQuestion.number == 14 || mQuestion.number == 15) {
            Handler().postDelayed({
                txtcounsel1?.text = "Tôi Chọn Đáp Án  B"
            }, 800)
            Handler().postDelayed({
                txtcounsel2?.text = "Tôi Chọn Đáp Án  D"
            }, 1700)
            Handler().postDelayed({
                txtcounsel3?.text = "Tôi Chọn Đáp Án  A"
            }, 2600)
        }else {
            Handler().postDelayed({
                txtcounsel1?.text = "Tôi Chọn Đáp Án  D"
            }, 800)
            Handler().postDelayed({
                txtcounsel2?.text = "Tôi Chọn Đáp Án  D"
            }, 1700)
            Handler().postDelayed({
                txtcounsel3?.text = "Tôi Chọn Đáp Án  C"
            }, 2600)
        }

        val button: TextView? = dialog.findViewById(R.id.btnthank)
        button?.setOnClickListener {
            dialog.dismiss()
        }
    }
    //Hàm xử lí quyền trợ giúp 50 50
    private fun half50_50() {
        val txtanswer1 = findViewById<TextView>(R.id.txtanswer1)
        val txtanswer2 = findViewById<TextView>(R.id.txtanswer2)
        val txtanswer3 = findViewById<TextView>(R.id.txtanswer3)
        val txtanswer4 = findViewById<TextView>(R.id.txtanswer4)
        if(mQuestion.number == 1 || mQuestion.number == 5 || mQuestion.number == 8 || mQuestion.number == 13) {
            Handler().postDelayed({
                txtanswer2.setText("")
                txtanswer3.setText("")
                txtanswer2.isEnabled = false
                txtanswer3.isEnabled = false
            }, 700)
        }else if(mQuestion.number == 2 || mQuestion.number == 6 || mQuestion.number == 9 || mQuestion.number == 15){
            Handler().postDelayed({
                txtanswer1.setText("")
                txtanswer4.setText("")
                txtanswer1.isEnabled = false
                txtanswer4.isEnabled = false
            }, 700)
        }else if(mQuestion.number == 7 || mQuestion.number == 10){
            Handler().postDelayed({
                txtanswer3.setText("")
                txtanswer4.setText("")
                txtanswer3.isEnabled = false
                txtanswer4.isEnabled = false
            }, 700)
        }
        else{
            Handler().postDelayed({
                txtanswer1.setText("")
                txtanswer3.setText("")
                txtanswer1.isEnabled = false
                txtanswer3.isEnabled = false
            }, 700)
        }
    }
    @SuppressLint("SetTextI18n")
    //Hàm xử lí quyền trợ giúp hỏi ý kiến khán giả trong trường quay -> chưa fix được
    private fun askAudien(prs1: Int, prs2: Int, prs3: Int, prs4: Int) {
        val buid = AlertDialog.Builder(this, R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.ask_audiencedialog, null)
        buid.setView(view)
        buid.setCancelable(false)
        dialog = buid.create()
        dialog.setCancelable(false)
        dialog.show()

        val txtpercentA: TextView? = dialog.findViewById(R.id.txtpercentA)
        val txtpercentB: TextView? = dialog.findViewById(R.id.txtpercentB)
        val txtpercentC: TextView? = dialog.findViewById(R.id.txtpercentC)
        val txtpercentD: TextView? = dialog.findViewById(R.id.txtpercentD)

        txtpercentA?.text = "$prs1%"
        txtpercentB?.text = "$prs2%"
        txtpercentC?.text = "$prs3%"
        txtpercentD?.text = "$prs4%"

        val progress1: ColumnProgressView? = dialog.findViewById(R.id.progress1)
        val progress2: ColumnProgressView? = dialog.findViewById(R.id.progress2)
        val progress3: ColumnProgressView? = dialog.findViewById(R.id.progress3)
        val progress4: ColumnProgressView? = dialog.findViewById(R.id.progress4)

        progress1?.setProgress(prs1)
        progress2?.setProgress(prs2)
        progress3?.setProgress(prs3)
        progress4?.setProgress(prs4)

        val button: TextView? = dialog.findViewById(R.id.btnthank)
        button?.setOnClickListener {
            dialog.dismiss()
        }
    }

    //set câu hỏi, tiêu đề, câu trả lời, màu lên xml
    @SuppressLint("SetTextI18n")
    private fun setDataQuestion(question: Question) {
        mQuestion = question

        //set lại màu khi chuyển sang câu hỏi tiếp theo
        tvAnswer1.setBackgroundResource(R.drawable.bg_time_answer_btn)
        tvAnswer2.setBackgroundResource(R.drawable.bg_time_answer_btn)
        tvAnswer3.setBackgroundResource(R.drawable.bg_time_answer_btn)
        tvAnswer4.setBackgroundResource(R.drawable.bg_time_answer_btn)

        val titleQuestion:String = "Câu hỏi " + question.number + "/15"
        findViewById<TextView>(R.id.txtmonney).text = "$ " + question.monney
        tvQuestion.setText(titleQuestion)
        tvContentQuestion.setText(question.content)
        tvAnswer1.setText(question.List_Answer.get(0).content)
        tvAnswer2.setText(question.List_Answer.get(1).content)
        tvAnswer3.setText(question.List_Answer.get(2).content)
        tvAnswer4.setText(question.List_Answer.get(3).content)

        tvAnswer1.setOnClickListener(this)
        tvAnswer2.setOnClickListener(this)
        tvAnswer3.setOnClickListener(this)
        tvAnswer4.setOnClickListener(this)
    }
    //ánh xạ tới các view
    private fun initUi() {
        tvQuestion = findViewById(R.id.txtquestion)
        tvContentQuestion = findViewById(R.id.txtcontent_question)
        tvAnswer1 = findViewById(R.id.txtanswer1)
        tvAnswer2 = findViewById(R.id.txtanswer2)
        tvAnswer3 = findViewById(R.id.txtanswer3)
        tvAnswer4 = findViewById(R.id.txtanswer4)

    }

    //Nạp tất cả câu hỏi và trả lời vào danh sách List
    private fun getListQuestion(): List<Question> {
        val list = mutableListOf<Question>()

        val answerList1 = mutableListOf<Answer>()
        answerList1.add(Answer("A: Tay Chân", true))
        answerList1.add(Answer("B: Chân tay", false))
        answerList1.add(Answer("C: Tay", false))
        answerList1.add(Answer("D: Chân", false)) /*A */

        val answerList2 = mutableListOf<Answer>()
        answerList2.add(Answer("A: Bơi lội", false))
        answerList2.add(Answer("B: Cầu lông", false))
        answerList2.add(Answer("C: Bóng đá", true))
        answerList2.add(Answer("D: Bóng chuyền", false)) /*C*/

        val answerList3 = mutableListOf<Answer>()
        answerList3.add(Answer("A: Đà Nẵng", false))
        answerList3.add(Answer("B: Huế", false))
        answerList3.add(Answer("C: TP Hồ Chí Minh", false))
        answerList3.add(Answer("D: Hà Nội", true))/*D*/

        val answerList4 = mutableListOf<Answer>()
        answerList4.add(Answer("A: Đà Điểu", false))
        answerList4.add(Answer("B: Chim Cánh Cụt", false))
        answerList4.add(Answer("C: Vịt Xiêm", false))
        answerList4.add(Answer("D: Tất Cả", true))/*D*/

        val answerList5 = mutableListOf<Answer>()
        answerList5.add(Answer("A: Trồng Cây", true))
        answerList5.add(Answer("B: Lông mày", false))
        answerList5.add(Answer("C: Hái", false))
        answerList5.add(Answer("D: Cho", false)) /*A */

        val answerList6 = mutableListOf<Answer>()
        answerList6.add(Answer("A: Nga", false))
        answerList6.add(Answer("B: Thụy Sĩ", false))
        answerList6.add(Answer("C: Việt Nam", true))
        answerList6.add(Answer("D: Pháp", false)) /*C*/

        val answerList7 = mutableListOf<Answer>()
        answerList7.add(Answer("A: Brazil", false))
        answerList7.add(Answer("B: Argentia", true))
        answerList7.add(Answer("C: Pháp", false))
        answerList7.add(Answer("D: Bồ Đào Nha", false))/*B*/

        val answerList8 = mutableListOf<Answer>()
        answerList8.add(Answer("A: Trường ca", true))
        answerList8.add(Answer("B: Hòa tấu nhạc cụ dân tộc", false))
        answerList8.add(Answer("C: Nhạc kịch", false))
        answerList8.add(Answer("D: Giao hưởng", false))/*A*/

        val answerList9 = mutableListOf<Answer>()
        answerList9.add(Answer("A: Nga", false))
        answerList9.add(Answer("B: Mỹ", false))
        answerList9.add(Answer("C: Trung Quốc", true))
        answerList9.add(Answer("D: Việt Nam", false))/*C*/

        val answerList10 = mutableListOf<Answer>()
        answerList10.add(Answer("A: Trong Cách mạng tháng Tám", false))
        answerList10.add(Answer("B: Trong kháng chiến chống Pháp", true))
        answerList10.add(Answer("C: Trong kháng chiến chống Mỹ", false))
        answerList10.add(Answer("D: Sau giải phóng", false))/*B*/

        val answerList11 = mutableListOf<Answer>()
        answerList11.add(Answer("A: - 29 độ C", false))
        answerList11.add(Answer("B: - 59 độ C", false))
        answerList11.add(Answer("C: - 49 độ C", false))
        answerList11.add(Answer("D: - 39 độ C", true))/*D*/

        val answerList12 = mutableListOf<Answer>()
        answerList12.add(Answer("A: Sa mạc Gobi", false))
        answerList12.add(Answer("B: Sa mạc Namib", false))
        answerList12.add(Answer("C: Sa mạc Kalahari", false))
        answerList12.add(Answer("D: Sa mạc Sahara", true))/*D*/

        val answerList13 = mutableListOf<Answer>()
        answerList13.add(Answer("A: 510", true))
        answerList13.add(Answer("B: 490", false))
        answerList13.add(Answer("C: 530", false))
        answerList13.add(Answer("D: 550", false))/*A*/

        val answerList14 = mutableListOf<Answer>()
        answerList14.add(Answer("A: Khí Neon (Ne)", false))
        answerList14.add(Answer("B: Khí Argon (Ar)", false))
        answerList14.add(Answer("C: Khí Kripton", false))
        answerList14.add(Answer("D: Khí Heli (He)", true))/*B*/

        val answerList15 = mutableListOf<Answer>()
        answerList15.add(Answer("A: Trái mít", false))
        answerList15.add(Answer("B: Trái mận", true))
        answerList15.add(Answer("C: Trái thị", false))
        answerList15.add(Answer("D: Trái cam", false))/*C*/

        list.add(Question(1, "Anh em như thể ... ", "200.000",answerList1))
        list.add(Question(2, "Môn nào là môn thể thao vua?","400.000", answerList2))
        list.add(Question(3, "Thủ đô của Việt Nam là gì?", "600.000", answerList3))
        list.add(Question(4, "Con gì có 2 chân?","1.000.000", answerList4))
        list.add(Question(5, "Ăn quả nhớ kẻ ... ", "2.000.000", answerList5))
        list.add(Question(6, "Nước nào ở Châu Á?", "3.000.000", answerList6))
        list.add(Question(7, "Lionel Messi là người nước nào?", "6.000.000",answerList7))
        list.add(Question(8, "Tác phẩm QUÊ HƯƠNG của cố nhạc sĩ Hoàng Việt thuộc thể loại nào?","10.000.000", answerList8))
        list.add(Question(9, "Bộ phim Tây Du Kí là của nước nào ?", "14.000.000", answerList9))
        list.add(Question(10, "Phong trào BA SẴN SÀNG ở miền Bắc nước ta ra đời trong thời gian nào?","22.000.000", answerList10))
        list.add(Question(11, "Ở áp suất thường, nhiệt độ đông đặc của thủy ngân lỏng là bao nhiêu độ bách phân?", "30.000.000", answerList11))
        list.add(Question(12, "Sa mạc nào được công nhận là một trong 7 kỳ quan thiên nhiên của châu Phi?", "40.000.000", answerList12))
        list.add(Question(13, "Đoạn sông Hồng chảy qua Việt Nam có độ dài bao nhiêu km? ", "60.000.000",answerList13))
        list.add(Question(14, "Trong 4 nguyên tố khí trơ dưới đây, nguyên tố nào có số electron ngoài cùng thấp nhất?","85.000.000", answerList14))
        list.add(Question(15, "Bà chúa thơ Nôm Hồ Xuân Hương có bài thơ ví thân phận người phụ nữ với loại trái cây nào?", "150.000.000", answerList15))

        return list
    }
    //Bắt đầu
    private fun playSound() {
        mediaPlayer?.start()
    }
    //
    override fun onDestroy() {
        super.onDestroy()
        // Giải phóng tài nguyên khi không cần sử dụng nữa
        mediaPlayer?.release()
        mediaPlayer = null
    }
    //xử lí sự kiện khi click vào answer
    override fun onClick(v: View?) {
        if (!isAnswerSelected) {
            if (v != null) {
                when (v.id) {
                    R.id.txtanswer1 -> {
                        tvAnswer1.setBackgroundResource(R.drawable.orange_corner_30)
                        countDownTimer.cancel()
                        CheckAnswer(tvAnswer1, mQuestion, mQuestion.List_Answer.get(0))
                        // Khi click vào câu trả lời thì tiếng tick tack sau 10 giây sẽ dừng
                        mediaPlayer?.pause()
                        isAnswerSelected = true // Đánh dấu đã chọn câu trả lời
                    }
                    R.id.txtanswer2 -> {
                        tvAnswer2.setBackgroundResource(R.drawable.orange_corner_30)
                        CheckAnswer(tvAnswer2, mQuestion, mQuestion.List_Answer.get(1))
                        countDownTimer.cancel()
                        mediaPlayer?.pause()
                        isAnswerSelected = true // Đánh dấu đã chọn câu trả lời
                    }
                    R.id.txtanswer3 -> {
                        tvAnswer3.setBackgroundResource(R.drawable.orange_corner_30)
                        CheckAnswer(tvAnswer3, mQuestion, mQuestion.List_Answer.get(2))
                        countDownTimer.cancel()
                        mediaPlayer?.pause()
                        isAnswerSelected = true // Đánh dấu đã chọn câu trả lời
                    }
                    R.id.txtanswer4 -> {
                        tvAnswer4.setBackgroundResource(R.drawable.orange_corner_30)
                        CheckAnswer(tvAnswer4, mQuestion, mQuestion.List_Answer.get(3))
                        countDownTimer.cancel()
                        mediaPlayer?.pause()
                        isAnswerSelected = true // Đánh dấu đã chọn câu trả lời
                    }
                }
            }
        }
    }
    //Kiểm tra và set lại background
    private fun CheckAnswer(textview: TextView, question: Question, answer: Answer){
         fun run() {
            if(answer.isCorrect){
                textview.setBackgroundResource(R.drawable.bg_green_corner_30)
                mediaPlayer = MediaPlayer.create(this, R.raw.traloidung)
                // Phát âm thanh khi cần
                playSound()
                nextQuestion()
            }else{
                textview.setBackgroundResource(R.drawable.bg_red_corner_10)
                mediaPlayer = MediaPlayer.create(this, R.raw.traloisai)
                // Phát âm thanh khi cần
                playSound()
                showAnswerCorect(question)
                gameOver()
            }
        }
        Handler().postDelayed({
            // Mã để thực thi sau một khoảng thời gian chờ
            run()
        }, 1400)
    }

    //set lại màu background sau khi click vào answer true
    private fun showAnswerCorect(question: Question) {

        if(question.List_Answer.get(0).isCorrect){
            tvAnswer1.setBackgroundResource(R.drawable.bg_green_corner_30)
        }else if(question.List_Answer.get(1).isCorrect){
            tvAnswer2.setBackgroundResource(R.drawable.bg_green_corner_30)
        }else if(question.List_Answer.get(2).isCorrect){
            tvAnswer3.setBackgroundResource(R.drawable.bg_green_corner_30)
        }else if(question.List_Answer.get(3).isCorrect){
            tvAnswer4.setBackgroundResource(R.drawable.bg_green_corner_30)
        }
    }

    //Chuyển sang câu hỏi tiếp theo
    private fun nextQuestion() {
        if(currentQuestion == mListQuestion.size - 1){
            mediaPlayer?.pause()
            mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.win)
            playSound()
            Handler().postDelayed({
                showDialog()
            }, 1200)
        }else{
            currentQuestion++
            val txtanswer1 = findViewById<TextView>(R.id.txtanswer1)
            val txtanswer2 = findViewById<TextView>(R.id.txtanswer2)
            val txtanswer3 = findViewById<TextView>(R.id.txtanswer3)
            val txtanswer4 = findViewById<TextView>(R.id.txtanswer4)
            txtanswer1.isEnabled = true
            txtanswer2.isEnabled = true
            txtanswer3.isEnabled = true
            txtanswer4.isEnabled = true
            Handler().postDelayed({
                setDataQuestion(mListQuestion.get(currentQuestion))
                resetCountdown()
                isAnswerSelected = false
                isPlayedSound = false
            }, 1500)
        }
        previousMoney = findViewById<TextView>(R.id.txtmonney).text.toString()
    }

    private fun nextQuestion_helpNext() {
        if(currentQuestion == mListQuestion.size - 1){
            Handler().postDelayed({
                showDialog()
            }, 1200)
        }else{
            currentQuestion++
            val txtanswer1 = findViewById<TextView>(R.id.txtanswer1)
            val txtanswer2 = findViewById<TextView>(R.id.txtanswer2)
            val txtanswer3 = findViewById<TextView>(R.id.txtanswer3)
            val txtanswer4 = findViewById<TextView>(R.id.txtanswer4)
            txtanswer1.isEnabled = true
            txtanswer2.isEnabled = true
            txtanswer3.isEnabled = true
            txtanswer4.isEnabled = true
            mediaPlayer?.pause()
            Handler().postDelayed({
                setDataQuestion(mListQuestion.get(currentQuestion))
                resetCountdown()
                isAnswerSelected = false
            }, 800)
        }
    }
    //tạo dialog hiển thị sau khi thua
    private fun gameOver() {
        Handler().postDelayed({
            showDialog2()
        }, 1000)
    }
    private fun Timeout(){
        Handler().postDelayed({
            showDialog2()
        }, 200)
    }
    //xử lí dialog
    @SuppressLint("SetTextI18n")
    private fun showDialog(){
        val buid = AlertDialog.Builder(this, R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.customdialog, null)
        buid.setView(view)
        buid.setCancelable(false)
        //code chơi lại
        val btnThamgia = view.findViewById<Button>(R.id.btnchoilai)
        btnThamgia.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            mediaPlayer?.pause()
        }
        dialog = buid.create()
        dialog.show()
        val txtend = view.findViewById<TextView>(R.id.txtend)
        val txtmonney = findViewById<TextView>(R.id.txtmonney)
        txtend.text = "Chúc mừng, bạn đã chiến thắng! Bạn sẽ ra về với tiền thưởng lớn " + txtmonney.text + " đồng"
        // code close
//        val btnClose = view.findViewById<ImageButton>(R.id.btnclose)
//        btnClose.setOnClickListener { dialog.dismiss() }
    }
    @SuppressLint("SetTextI18n")
    private fun showDialog2(){
        val buid = AlertDialog.Builder(this, R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.customdialog, null)
        buid.setView(view)
        buid.setCancelable(false)
        //code chơi lại
        val btnThamgia = view.findViewById<Button>(R.id.btnchoilai)
        btnThamgia.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        dialog = buid.create()
        dialog.show()

        val txtend = view.findViewById<TextView>(R.id.txtend)
        findViewById<TextView>(R.id.txtmonney)
        txtend.text = "Bạn sẽ ra về với số tiền $previousMoney đồng"
        // code close
//        val btnClose = view.findViewById<ImageButton>(R.id.btnclose)
//        btnClose.setOnClickListener { dialog.dismiss() }
    }

    //hay lỗi
    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                // Cập nhật giao diện hiển thị thời gian còn lại
                updateCountdownText()
                if (millisUntilFinished <= 10000 && !isPlayedSound) {
                    mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.demnguoctime)
                    playSound()
                    isPlayedSound = true
                }
            }
            override fun onFinish() {
                mediaPlayer = MediaPlayer.create(this@Manhinh2, R.raw.traloisai)
                playSound()
                Timeout()
            }
        }
        countDownTimer.start()
    }
    //Hiển thị số giây còn lại
    private fun updateCountdownText() {
        val secondsLeft = timeLeftInMillis / 1000
        val txttime = findViewById<TextView>(R.id.textViewTimeLeft)
        txttime.text = secondsLeft.toString()
    }

    //reset lại thời gian
    private fun resetCountdown() {
        // Đặt lại thời gian đếm ngược về mặc định (30 giây)
        timeLeftInMillis = 30000
        // Bắt đầu đếm ngược lại cho câu hỏi mới
        countDownTimer.start()
    }
}