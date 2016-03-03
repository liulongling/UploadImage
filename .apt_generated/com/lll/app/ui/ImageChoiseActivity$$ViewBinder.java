// Generated code from Butter Knife. Do not modify!
package com.lll.app.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ImageChoiseActivity$$ViewBinder<T extends com.lll.app.ui.ImageChoiseActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361797, "field 'mBottomLy'");
    target.mBottomLy = finder.castView(view, 2131361797, "field 'mBottomLy'");
    view = finder.findRequiredView(source, 2131361801, "field 'mPreview' and method 'onClick'");
    target.mPreview = finder.castView(view, 2131361801, "field 'mPreview'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361799, "field 'notSelect' and method 'onClick'");
    target.notSelect = finder.castView(view, 2131361799, "field 'notSelect'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361795, "field 'mImageCount'");
    target.mImageCount = finder.castView(view, 2131361795, "field 'mImageCount'");
    view = finder.findRequiredView(source, 2131361800, "field 'mBaseImageSize'");
    target.mBaseImageSize = finder.castView(view, 2131361800, "field 'mBaseImageSize'");
    view = finder.findRequiredView(source, 2131361798, "field 'mChooseDir' and method 'onClick'");
    target.mChooseDir = finder.castView(view, 2131361798, "field 'mChooseDir'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361796, "field 'mGirdView'");
    target.mGirdView = finder.castView(view, 2131361796, "field 'mGirdView'");
    view = finder.findRequiredView(source, 2131361794, "method 'onClick'");
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
    target.mBottomLy = null;
    target.mPreview = null;
    target.notSelect = null;
    target.mImageCount = null;
    target.mBaseImageSize = null;
    target.mChooseDir = null;
    target.mGirdView = null;
  }
}
