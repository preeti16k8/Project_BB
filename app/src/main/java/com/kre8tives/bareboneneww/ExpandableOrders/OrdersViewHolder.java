package com.kre8tives.bareboneneww.ExpandableOrders;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kre8tives.bareboneneww.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class OrdersViewHolder extends GroupViewHolder {

        private TextView OrderId,OrderDate,OrderStatus,OrderTotal;
        private ImageView arrow;

        public OrdersViewHolder(View itemView) {
            super(itemView);
            OrderId = itemView.findViewById(R.id.expaorderId);
            OrderDate=itemView.findViewById(R.id.expaorderDate);
            OrderStatus=itemView.findViewById(R.id.expaorderStatus);
            OrderTotal=itemView.findViewById(R.id.expaorderAmount);
            arrow=itemView.findViewById(R.id.arrow);

        }

        public void setGenreTitle(ExpandableGroup group) {
            OrderId.setText(group.getTitle());
            OrderDate.setText((((ExpaOrders) group).getOrderDate()));
            OrderStatus.setText((((ExpaOrders) group).getOrderstatus()));
            OrderTotal.setText((((ExpaOrders) group).getOrdertotal()));
            arrow.setBackgroundResource((((ExpaOrders) group).getIconResId()));

        }
    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

}