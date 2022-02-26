package com.efficacious.e_smartsrestaurant.Menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.MenuCategoryDetail;
import com.efficacious.e_smartsrestaurant.NewOrder.SelectMenuFragment;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder>{

    List<MenuCategoryDetail> menuCategoryDetail;
    Context context;

    public MenuCategoryAdapter(List<MenuCategoryDetail> menuCategoryDetail) {
        this.menuCategoryDetail = menuCategoryDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String CategoryName = menuCategoryDetail.get(position).getCatName();
        holder.categoryName.setText(menuCategoryDetail.get(position).getCatName());
        String CatId = String.valueOf(menuCategoryDetail.get(position).getCatId());
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("CategoryId", CatId);
                Fragment fragment = new MenuListFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                        .addToBackStack(null).commit();
            }
        });

        categoryImg(holder.categoryImg,CategoryName);
    }

    private void categoryImg(ImageView categoryImg, String categoryName) {
        if (categoryName.equalsIgnoreCase("South Indian")){
            categoryImg.setImageResource(R.drawable.south_indian);
        }else if (categoryName.equalsIgnoreCase("Maharashtrian")){
            categoryImg.setImageResource(R.drawable.maharashtrian);
        }else if (categoryName.equalsIgnoreCase("Pizzas")){
            categoryImg.setImageResource(R.drawable.pizza);
        }else if (categoryName.equalsIgnoreCase("Sandwich")){
            categoryImg.setImageResource(R.drawable.sandwich);
        }else if (categoryName.equalsIgnoreCase("Beverages")){
            categoryImg.setImageResource(R.drawable.beverages);
        }else if (categoryName.equalsIgnoreCase("Refreshers")){
            categoryImg.setImageResource(R.drawable.refreshers);
        }else if (categoryName.equalsIgnoreCase("Faloodas")){
            categoryImg.setImageResource(R.drawable.faloodas);
        }else if (categoryName.equalsIgnoreCase("Fresh-N-Juicy")){
            categoryImg.setImageResource(R.drawable.fresh_n_juicy);
        }else if (categoryName.equalsIgnoreCase("Shaken Up")){
            categoryImg.setImageResource(R.drawable.shaken_up);
        }else if (categoryName.equalsIgnoreCase("Salad/Raita/Papad")){
            categoryImg.setImageResource(R.drawable.salad_raita_papad);
        }else if (categoryName.equalsIgnoreCase("Soups")){
            categoryImg.setImageResource(R.drawable.soup);
        }else if (categoryName.equalsIgnoreCase("Starter")){
            categoryImg.setImageResource(R.drawable.starter);
        }else if (categoryName.equalsIgnoreCase("Main Course")){
            categoryImg.setImageResource(R.drawable.main_course);
        }else if (categoryName.equalsIgnoreCase("Chinese")){
            categoryImg.setImageResource(R.drawable.chinese);
        }else if (categoryName.equalsIgnoreCase("Rice")){
            categoryImg.setImageResource(R.drawable.rice);
        }else if (categoryName.equalsIgnoreCase("Continental")){
            categoryImg.setImageResource(R.drawable.continental);
        }else if (categoryName.equalsIgnoreCase("Ice Creams")){
            categoryImg.setImageResource(R.drawable.ice_cream);
        }else if (categoryName.equalsIgnoreCase("Dessert")){
            categoryImg.setImageResource(R.drawable.dessert);
        }else{
            categoryImg.setImageResource(R.drawable.pasta);
        }
    }

    @Override
    public int getItemCount() {
        return menuCategoryDetail.size();
    }

    public void updateList(List<MenuCategoryDetail> temp) {
        menuCategoryDetail = temp;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImg;
        TextView categoryName;
        Button btnView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImg = itemView.findViewById(R.id.categoryImg);
            categoryName = itemView.findViewById(R.id.categoryName);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
