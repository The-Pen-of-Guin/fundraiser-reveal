package gic.node;

import java.time.Instant;

import gic.currrency.Currency;
import gic.node.effect.ScreenEffect;
import gic.node.effect.TextEffect;

public record AnimationNode(Instant startTime, Instant endTime, Currency targetAmount, TextEffect textEffect, ScreenEffect screenEffect) {
}
