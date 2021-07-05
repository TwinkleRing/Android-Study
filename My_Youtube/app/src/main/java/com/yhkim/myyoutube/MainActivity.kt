package com.yhkim.myyoutube

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.yhkim.myyoutube.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var glide : RequestManager
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as MasterApplication).service.getYouTubeList()
            .enqueue(object : Callback<ArrayList<Youtube>> {
                override fun onFailure(call: Call<ArrayList<Youtube>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ArrayList<Youtube>>,
                    response: Response<ArrayList<Youtube>>
                ) {
                    if (response.isSuccessful) {
                        glide = Glide.with(this@MainActivity)
                        val youtubeList = response.body()
                        val adapter = YoutubeAdapter(
                            youtubeList!!,
                            LayoutInflater.from(this@MainActivity),
                            glide,
                            this@MainActivity
                        )
                        binding.youtubeListRecycler.adapter = adapter
                    }
                }
            })
    }
}



class YoutubeAdapter(

    var youtubeList : ArrayList<Youtube>,
    val inflater: LayoutInflater,
    val glide : RequestManager,
    val activity: Activity // 영상을 누르면 재생시키기 위해 intent 사용, Activity 사용

) : RecyclerView.Adapter<YoutubeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView
        val thumbnail : ImageView
        val content : TextView

        init {
            title = itemView.findViewById(R.id.youtube_title)
            thumbnail = itemView.findViewById(R.id.youtube_thumbnail)
            content = itemView.findViewById(R.id.youtube_content)

            itemView.setOnClickListener {
                val position : Int = adapterPosition
                val intent = Intent(activity, YouTubeDetailActivity::class.java)
                intent.putExtra("video_url", youtubeList.get(position).video)
                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return youtubeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(youtubeList.get(position).title)
        holder.content.setText(youtubeList.get(position).content)
        glide.load(youtubeList.get(position).thumbnail).into(holder.thumbnail)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.youtube_item_view, parent, false)
        return ViewHolder(view)
    }
}