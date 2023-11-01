package com.example.pocketinventory;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketinventory.Expense;
import com.example.pocketinventory.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> data;
    private Context context;

    public ExpenseAdapter(Context context, List<Expense> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense expense = data.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView makeTextView;
        private TextView modelTextView;
        private TextView descriptionTextView;
        private TextView valueTextView;
        private TextView commentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            makeTextView = itemView.findViewById(R.id.makeTextView);
            modelTextView = itemView.findViewById(R.id.modelTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }

        public void bind(Expense expense) {
            dateTextView.setText("Date: " + expense.getDate());
            makeTextView.setText("Make: " + expense.getMake());
            modelTextView.setText("Model: " + expense.getModel());
            descriptionTextView.setText("Description: " + expense.getDescription());
            valueTextView.setText("Value: $" + expense.getValue());
            commentTextView.setText("Comment: " + expense.getComment());
        }
    }
}

