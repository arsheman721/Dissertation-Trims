package barber.ahmad.com.trims.Adapter;

import android.content.Context;
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

import barber.ahmad.com.trims.R;
import barber.ahmad.com.trims.model.appointmentclass;
import barber.ahmad.com.trims.model.barbersclass;
import barber.ahmad.com.trims.utils.ActionListner;

public class appointmentAdapter extends RecyclerView.Adapter<appointmentAdapter.BarbersHolder> {
ArrayList<appointmentclass> appointmentclassArrayList;
    AdapterView.OnItemClickListener onItemClickListener;
    ActionListner listner;
    Context context;
    String type;
    public appointmentAdapter(ArrayList<appointmentclass> appointmentclassArrayList, AdapterView.OnItemClickListener onItemClickListener,ActionListner listner,Context context,String type)
    {
        this.appointmentclassArrayList=appointmentclassArrayList;
        this.onItemClickListener=onItemClickListener;
        this.listner=listner;
        this.context=context;
        this.type=type;
    }
    public appointmentAdapter(ArrayList<appointmentclass> appointmentclassArrayList, AdapterView.OnItemClickListener onItemClickListener,Context context,String type)
    {
        this.appointmentclassArrayList=appointmentclassArrayList;
        this.onItemClickListener=onItemClickListener;
        this.listner=null;
        this.context=context;
        this.type=type;
    }
    @NonNull
    @Override
    public BarbersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.layout_appointment_file, parent,false);

        return new BarbersHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarbersHolder holder, final int position) {

        if(type.equals("user"))
        {
            holder.ivBarberImage.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.tvCustomername.setText("Name :"+appointmentclassArrayList.get(position).bname);
        }
        else if(type.equals("barber"))
        {
            holder.ivBarberImage.setImageDrawable(context.getResources().getDrawable(R.drawable.beardman));
            holder.tvCustomername.setText("Name :"+appointmentclassArrayList.get(position).cname);
        }
        holder.tvDate.setText("Date :"+appointmentclassArrayList.get(position).appdate);
        holder.tvStatus.setText("Status :"+appointmentclassArrayList.get(position).status);
        if(appointmentclassArrayList.get(position).status.equals("pending"))
        {
//            holder.tvnote.setVisibility(View.VISIBLE);
            if(listner!=null)
            {

                holder.ivPending.setVisibility(View.GONE);
                holder.ivApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listner.updateAppointment(appointmentclassArrayList.get(position).appid,"approved");
                    }
                });
                holder.ivReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listner.updateAppointment(appointmentclassArrayList.get(position).appid,"cancel");
                    }
                });
            }
            else
            {
//                holder.ivBarberImage.setImageDrawable(context.getResources().getDrawable(R.drawable.beardman));
                holder.ivReject.setVisibility(View.GONE);
                holder.ivApprove.setVisibility(View.GONE);
            }

        }
        else if(appointmentclassArrayList.get(position).status.equals("approved"))
        {
            holder.ivReject.setVisibility(View.GONE);
            holder.ivPending.setVisibility(View.GONE);
        }
        else if(appointmentclassArrayList.get(position).status.equals("cancel"))
        {
            holder.ivApprove.setVisibility(View.GONE);
            holder.ivPending.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null,holder.itemView,position, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentclassArrayList.size();
    }

    public class BarbersHolder extends RecyclerView.ViewHolder{

        ImageView ivBarberImage,ivApprove,ivReject,ivPending;
        TextView tvCustomername,tvDate,tvStatus,tvnote;


        public BarbersHolder(@NonNull View itemView) {
            super(itemView);

            ivBarberImage=itemView.findViewById(R.id.iv_barber);
            ivApprove=itemView.findViewById(R.id.approve);
            ivReject=itemView.findViewById(R.id.reject);
            ivPending=itemView.findViewById(R.id.pending);
            tvCustomername=itemView.findViewById(R.id.tv_customername);
            tvDate=itemView.findViewById(R.id.tv_date);
            tvStatus=itemView.findViewById(R.id.tv_status);
            tvnote=itemView.findViewById(R.id.tv_note);
        }
    }
}
