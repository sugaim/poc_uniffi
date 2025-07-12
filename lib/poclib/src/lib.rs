use statrs::distribution::ContinuousCDF;

pub fn bs_call_prem(spot: f64, strike: f64, vol: f64, rate: f64, time: f64) -> f64 {
    // Black-Scholes formula for European call option pricing
    assert!(0.0 <= time);
    assert!(0.0 < spot);
    assert!(0.0 < strike);
    let fwd = spot * (-rate * time).exp();
    let tot_var = vol * vol * time;

    let d1 = (fwd.ln() - strike.ln() + 0.5 * tot_var) / tot_var.sqrt();
    let d2 = d1 - tot_var.sqrt();

    let dist = statrs::distribution::Normal::new(0.0, 1.0).unwrap();
    let norm_cdf = |x: f64| dist.cdf(x);
    let fwdprem = fwd * norm_cdf(d1) - strike * norm_cdf(d2);
    let discount = (-rate * time).exp();
    fwdprem * discount
}

pub fn bs_put_prem(spot: f64, strike: f64, vol: f64, rate: f64, time: f64) -> f64 {
    // Put option price using put-call parity
    let call_prem = bs_call_prem(spot, strike, vol, rate, time);
    let fwd = spot * (-rate * time).exp();
    strike - fwd + call_prem
}
