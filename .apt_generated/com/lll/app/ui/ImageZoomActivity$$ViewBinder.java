// Generated code from Butter Knife. Do not modify!
package com.lll.app.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ImageZoomActivity$$ViewBinder<T extends com.lll.app.ui.ImageZoomActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361794, "field 'uploadLayout' and method 'onClick'");
    target.uploadLayout = finder.castView(view, 2131361794, "field 'uploadLayout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361803, "field 'pager'");
    target.pager = finder.castView(view, 2131361803, "field 'pager'");
    view = finder.findRequiredView(source, 2131361806, "field 'bottombarLayout'");
    target.bottombarLayout = finder.castView(view, 2131361806, "field 'bottombarLayout'");
    view = finder.findRequiredView(source, 2131361807, "field 'mBaseImage' and method 'onClick'");
    target.mBaseImage = finder.castView(view, 2131361807, "field 'mBaseImage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361805, "field 'mDelete' and method 'onClick'");
    target.mDelete = finder.castView(view, 2131361805, "field 'mDelete'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361804, "field 'mPos'");
    target.mPos = finder.castView(view, 2131361804, "field 'mPos'");
    view = finder.findRequiredView(source, 2131361792, "field 'titlebarLayout'");
    target.titlebarLayout = finder.castView(view, 2131361792, "field 'titlebarLayout'");
    view = finder.findRequiredView(source, 2131361793, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.uploadLayout = null;
    target.pager = null;
    target.bottombarLayout = null;
    target.mBaseImage = null;
    target.mDelete = null;
    target.mPos = null;
    target.titlebarLayout = null;
  }
}
