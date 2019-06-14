package barber.ahmad.com.trims;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import barber.ahmad.com.trims.model.barbersclass;

public class BarbersAdapter extends RecyclerView.Adapter<BarbersAdapter.BarbersHolder> {
ArrayList<barbersclass> barbersclassArrayList;
    AdapterView.OnItemClickListener onItemClickListener;

    public BarbersAdapter(ArrayList<barbersclass> barbersclassArrayList, AdapterView.OnItemClickListener onItemClickListener)
    {
        this.barbersclassArrayList=barbersclassArrayList;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public BarbersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.layout_barbers_file, parent,false);

        return new BarbersHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarbersHolder holder, final int position) {

        if(!barbersclassArrayList.get(position).imagelink.equals(""))
        Glide.with(holder.itemView.getContext())
                .load(barbersclassArrayList.get(position).imagelink)
                .placeholder(R.drawable.user)
                .into(holder.ivBarberImage);


        holder.tvBarberName.setText(barbersclassArrayList.get(position).name);
        holder.tvBarberEmail.setText(barbersclassArrayList.get(position).email);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null,holder.itemView,position, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barbersclassArrayList.size();
    }

    public class BarbersHolder extends RecyclerView.ViewHolder{

        ImageView ivBarberImage;
        TextView tvBarberName,tvBarberEmail;


        public BarbersHolder(@NonNull View itemView) {
            super(itemView);

            ivBarberImage=itemView.findViewById(R.id.iv_barber);
            tvBarberName=itemView.findViewById(R.id.tv_barber_name);
            tvBarberEmail=itemView.findViewById(R.id.tv_barber_email);
        }
    }
}
