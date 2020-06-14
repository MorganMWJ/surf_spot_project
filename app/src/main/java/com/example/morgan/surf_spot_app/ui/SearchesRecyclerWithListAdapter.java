package com.example.morgan.surf_spot_app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.morgan.surf_spot_app.R;
import com.example.morgan.surf_spot_app.model.db.Search;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchesRecyclerWithListAdapter extends
        RecyclerView.Adapter<SearchesRecyclerWithListAdapter.ViewHolder>{

    private List<Search> searches;
    private static SearchesRecyclerViewClickListener itemListener;

    SearchesRecyclerWithListAdapter(List<Search> searches, SearchesRecyclerViewClickListener itemListener){
        this.searches = searches;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public SearchesRecyclerWithListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* create a view holder with its view as the relative layout for a recycled item */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchesRecyclerWithListAdapter.ViewHolder holder, int i) {
        /* delegate this to viewholder inner class */
        if(searches!=null){
            holder.bindDataSet(searches.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (searches != null) {
            return searches.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView latLongView;
        TextView radiusView;
        TextView typeView;
        TextView surfKeywordView;
        TextView searchTimeView;
        Search search;

        ViewHolder(View itemView){
            super(itemView);
            latLongView = itemView.findViewById(R.id.recent_searches_location_body);
            radiusView = itemView.findViewById(R.id.recent_searches_radius_body);
            typeView = itemView.findViewById(R.id.recent_searches_place_type_body);
            surfKeywordView = itemView.findViewById(R.id.recent_searches_use_surf_keyword_body);
            searchTimeView = itemView.findViewById(R.id.recent_searches_search_time_body);

            /* Alert activity when user clicks on this ViewHolder */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.searchesRecyclerViewListClicked(search);
                }
            });
        }

        public Search getSearch(){
            return search;
        }

        void bindDataSet(Search s){
            latLongView.setText(s.getLocationString());
            radiusView.setText(s.getRadius()+"");
            typeView.setText(s.getType());
            surfKeywordView.setText(s.getUseSurfKeyword() ? "Yes" : "No");
            searchTimeView.setText(s.getSearchTime().toString());
            this.search = s;
        }
    }
}
