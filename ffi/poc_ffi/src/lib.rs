use std::collections::HashMap;

use poclib::{bs_call_prem, bs_put_prem};

// Error handling with proc-macros
#[derive(thiserror::Error, Debug, uniffi::Error)]
pub enum Error {
    #[error("Error: {msg}")]
    Generic { msg: String },
}

#[uniffi::export]
pub fn bs_call(spot: f64, strike: f64, vol: f64, rate: f64, time: f64) -> Result<f64, Error> {
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
) -> Result<f64, Error> {
    validate(model.spot, strike, time)?;
    match option_type {
        OptionType::Call => Ok(bs_call_prem(model.spot, strike, model.vol, rate, time)),
        OptionType::Put => Ok(bs_put_prem(model.spot, strike, model.vol, rate, time)),
    }
}

// HashMap support for dictionaries like {"hoge": 42, "fuga": 24}
#[uniffi::export]
pub fn sum_values(data: HashMap<String, i32>) -> Result<i32, Error> {
    let sum: i32 = data.values().sum();
    Ok(sum)
}

fn validate(spot: f64, strike: f64, time: f64) -> Result<(), Error> {
    if spot <= 0.0 {
        return Err(Error::Generic {
            msg: "Spot price must be greater than 0".to_string(),
        });
    }
    if strike <= 0.0 {
        return Err(Error::Generic {
            msg: "Strike price must be greater than 0".to_string(),
        });
    }
    if time < 0.0 {
        return Err(Error::Generic {
            msg: "Time must be non-negative".to_string(),
        });
    }
    Ok(())
}

// This generates all the bindings automatically - no UDL needed!
uniffi::setup_scaffolding!();
