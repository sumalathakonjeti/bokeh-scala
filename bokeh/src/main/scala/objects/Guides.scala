package io.continuum.bokeh

abstract class GuideRenderer extends Renderer {
    object plot extends Field[Plot]
    object bounds extends Field[(Double, Double)] // Either[Auto, (Float, Float)]]
}

abstract class Axis extends GuideRenderer {
    object location extends Field[Location]

    def defaultTicker: Ticker
    def defaultFormatter: TickFormatter

    object ticker extends Field[Ticker](defaultTicker)
    object formatter extends Field[TickFormatter](defaultFormatter)

    object axis_label extends Field[String]
    object axis_label_standoff extends Field[Int]
    //// object axis_label_props extends Include(TextProps, prefix="axis_label")

    object major_label_standoff extends Field[Int]
    object major_label_orientation extends Field[Orientation] // Either[Orientation, Double]
    //// object major_label_props extends Include(TextProps, prefix="major_label")

    //// object axis_props extends Include(LineProps, prefix="axis")
    //// object tick_props extends Include(LineProps, prefix="major_tick")

    object major_tick_in extends Field[Int]
    object major_tick_out extends Field[Int]
}

abstract class ContinuousAxis extends Axis

class LinearAxis extends ContinuousAxis {
    def defaultTicker: Ticker = new BasicTicker()
    def defaultFormatter: TickFormatter = new BasicTickFormatter()
}

class LogAxis extends ContinuousAxis {
    def defaultTicker: Ticker = new LogTicker().num_minor_ticks(10)
    def defaultFormatter: TickFormatter = new LogTickFormatter()
}

class CategoricalAxis extends Axis {
    def defaultTicker: Ticker = new CategoricalTicker()
    def defaultFormatter: TickFormatter = new CategoricalTickFormatter()
}

class DatetimeAxis extends LinearAxis {
    override def defaultTicker: Ticker = new DatetimeTicker()
    override def defaultFormatter: TickFormatter = new DatetimeTickFormatter()

    object scale extends Field[String]("time")
    object num_labels extends Field[Int](8)
    object char_width extends Field[Int](10)
    object fill_ratio extends Field[Double](0.3)
}

class Grid extends GuideRenderer {
    object dimension extends Field[Int](0)
    object ticker extends Field[Ticker]

    def axis(axis: Axis): SelfType = {
        axis.ticker.valueOpt.foreach(this.ticker := _)
        this
    }

    //// object grid_props extends Include(LineProps, prefix="grid")
}
