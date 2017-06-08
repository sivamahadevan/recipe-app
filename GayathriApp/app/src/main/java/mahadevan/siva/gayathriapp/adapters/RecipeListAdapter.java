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
import mahadevan.siva.gayathriapp.activities.RecipeDetailActivity;
import mahadevan.siva.gayathriapp.models.Recipe;

/**
 * Created by siva on 2016-12-15.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.CardViewHolder> {

    private List<Recipe> recipes;
    private Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView avatar;

        public CardViewHolder (View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.recipe_name);
            avatar = (ImageView) view.findViewById(R.id.recipe_avatar);
        }
    }

    public RecipeListAdapter (Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeListAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new RecipeListAdapter.CardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecipeListAdapter.CardViewHolder holder, final int position) {
        final Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.getName());
        Ion.with(holder.avatar).load(recipe.getAvatar());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("recipe", recipe);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
