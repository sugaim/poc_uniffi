use std::sync::Arc;

use poclib::{bs_call_prem, bs_put_prem};

uniffi::setup_scaffolding!();

#[derive(thiserror::Error, Debug, uniffi::Object)]
#[uniffi::export(Debug, Display)]
#[error("Error: {msg}")]
pub struct Error {
    msg: String,
}

#[uniffi::export]
pub fn bs_call(spot: f64, strike: f64, vol: f64, rate: f64, time: f64) -> Result<f64, Arc<Error>> {
    validate(spot, strike, time)?;
    Ok(bs_call_prem(spot, strike, vol, rate, time))
}

#[derive(uniffi::Record)]
pub struct BsModel {
    pub spot: f64,
    pub vol: f64,
}

#[derive(uniffi::Enum)]
pub enum OptionType {
    Call,
    Put,
}

#[uniffi::export]
pub fn bs_prem(
    model: BsModel,
    strike: f64,
    rate: f64,
    time: f64,
    option_type: OptionType,
) -> Result<f64, Arc<Error>> {
    validate(model.spot, strike, time)?;
    match option_type {
        OptionType::Call => Ok(bs_call_prem(model.spot, strike, model.vol, rate, time)),
        OptionType::Put => Ok(bs_put_prem(model.spot, strike, model.vol, rate, time)),
    }
}

#[uniffi::export]
pub fn sum_values(data: std::collections::HashMap<String, i32>) -> Result<i32, Arc<Error>> {
    let sum: i32 = data.into_iter().map(|(_, v)| v).sum();
    Ok(sum)
}

fn validate(spot: f64, strike: f64, time: f64) -> Result<(), Arc<Error>> {
    if spot <= 0.0 {
        return Err(Error {
            msg: "Spot price must be greater than 0".to_string(),
        }
        .into());
    }
    if strike <= 0.0 {
        return Err(Error {
            msg: "Strike price must be greater than 0".to_string(),
        }
        .into());
    }
    if time < 0.0 {
        return Err(Error {
            msg: "Time must be non-negative".to_string(),
        }
        .into());
    }
    Ok(())
}
