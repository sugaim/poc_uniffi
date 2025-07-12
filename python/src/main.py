

if __name__ == "__main__":
    import sys
    from external.poc_ffi import BsModel, OptionType, bs_prem

    try:
        model = BsModel(
            spot=100.0,
            vol=0.2,
        )
        payoff = OptionType.CALL
        strike = 100.0
        expiry = 1.0
        rate = 0.05
        price = bs_prem(model, strike, rate, expiry, payoff)
        print(f"Option Price: {price}")
    except Exception as e:
        print("An error occurred:", e)
        sys.exit(1)
