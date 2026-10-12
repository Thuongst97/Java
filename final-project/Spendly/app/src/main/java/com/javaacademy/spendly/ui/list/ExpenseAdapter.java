package com.javaacademy.spendly.ui.list;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.javaacademy.spendly.databinding.ItemExpenseBinding;
import com.javaacademy.spendly.domain.Expense;
import com.javaacademy.spendly.domain.Money;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Adapts a list of expenses to RecyclerView rows (lesson 19). {@link ListAdapter} diffs each new
 * list against the old one on a background thread and animates only what changed.
 */
public final class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.Holder> {

    private static final DiffUtil.ItemCallback<Expense> DIFF = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expense a, @NonNull Expense b) {
            return a.id() == b.id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense a, @NonNull Expense b) {
            return a.equals(b);            // records implement equals for us
        }
    };

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    private final Consumer<Expense> onClick;
    private String currency = "USD";

    public ExpenseAdapter(Consumer<Expense> onClick) {
        super(DIFF);
        this.onClick = onClick;
    }

    public void setCurrency(String code) {
        if (!code.equals(currency)) {
            currency = code;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    /** Exposed for swipe-to-delete. */
    public Expense itemAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(getItem(position));
    }

    final class Holder extends RecyclerView.ViewHolder {
        private final ItemExpenseBinding b;

        Holder(ItemExpenseBinding binding) {
            super(binding.getRoot());
            this.b = binding;
            b.getRoot().setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) onClick.accept(getItem(pos));
            });
        }

        void bind(Expense e) {
            b.emoji.setText(e.category().emoji());
            b.emoji.setBackgroundTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(e.category().color(), 70)));
            b.title.setText(e.title());
            String subtitle = e.category().displayName() + " · " + e.date().format(DATE);
            b.subtitle.setText(e.note().isEmpty() ? subtitle : subtitle + " · " + e.note());
            b.amount.setText(Money.format(e.amountCents(), currency, Locale.getDefault()));
        }
    }
}
