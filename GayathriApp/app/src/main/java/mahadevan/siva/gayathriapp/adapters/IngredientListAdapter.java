package mahadevan.siva.gayathriapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import mahadevan.siva.gayathriapp.R;
import mahadevan.siva.gayathriapp.activities.RecipeListActivity;
import mahadevan.siva.gayathriapp.models.Ingredient;

/**
 * Created by siva on 2016-12-14.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.CardViewHolder> {

    private List<Ingredient> ingredients;
    private Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView avatar;

        public CardViewHolder (View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.ingredient_name);
            avatar = (ImageView) view.findViewById(R.id.ingredient_avatar);
        }
    }

    public IngredientListAdapter (Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public IngredientListAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_card, parent, false);

        return new IngredientListAdapter.CardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(IngredientListAdapter.CardViewHolder holder, final int position) {
        final Ingredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        Ion.with(holder.avatar).load(ingredient.getAvatar());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeListActivity.class);
                intent.putExtra("ingredient", ingredient);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
