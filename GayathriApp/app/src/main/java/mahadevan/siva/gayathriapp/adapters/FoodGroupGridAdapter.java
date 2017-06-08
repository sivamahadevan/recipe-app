package mahadevan.siva.gayathriapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.List;

import mahadevan.siva.gayathriapp.R;
import mahadevan.siva.gayathriapp.activities.IngredientListActivity;
import mahadevan.siva.gayathriapp.models.FoodGroup;

/**
 * Created by siva on 2016-12-14.
 */

public class FoodGroupGridAdapter extends RecyclerView.Adapter<FoodGroupGridAdapter.CardViewHolder> {

    private List<FoodGroup> foodGroups;
    private Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView avatar;

        public CardViewHolder (View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.food_group_name);
            avatar = (ImageView) view.findViewById(R.id.food_group_avatar);
        }
    }

    public FoodGroupGridAdapter (Context context, List<FoodGroup> foodGroups) {
        this.context = context;
        this.foodGroups = foodGroups;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FoodGroupGridAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_group_card, parent, false);

        return new CardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
        final FoodGroup foodGroup = foodGroups.get(position);
        holder.name.setText(foodGroup.getName());
        Ion.with(holder.avatar).load(foodGroup.getAvatar());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IngredientListActivity.class);
                intent.putExtra("food_group_id", foodGroup.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodGroups.size();
    }
}
