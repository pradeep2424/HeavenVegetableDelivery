package com.heaven.vegetable.interfaces;

import com.github.vipulasri.timelineview.TimelineView;

public interface TimelineLineType {
    int NORMAL = 0;     // shows both start and end lines
    int START = 1;      // shows start line hides end line
    int END = 2;        // shows end line hides start line
    int ONLYONE = 3;    // hides both lines


//    if(viewType == TimelineView.LineType.START) {
//        showStartLine(false);
//        showEndLine(true);
//    } else if(viewType == TimelineView.LineType.END) {
//        showStartLine(true);
//        showEndLine(false);
//    } else if(viewType == TimelineView.LineType.ONLYONE) {
//        showStartLine(false);
//        showEndLine(false);
//    } else {
//        showStartLine(true);
//        showEndLine(true);
//    }
}
