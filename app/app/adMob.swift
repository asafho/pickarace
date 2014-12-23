//
//  adMob.swift
//  app
//
//  Created by Asaf Horovitz on 12/18/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

import Foundation
import UIKit
import iAd




class adMob: UIViewController, GADBannerViewDelegate, ADBannerViewDelegate {

    struct ads{
        static var interstitial:GADInterstitial?
    }
    //Interstitial func
    class func createAndLoadInterstitial()->GADInterstitial {
        println("trying loading interstitial ad")
        var interstitial = GADInterstitial()
        interstitial.adUnitID = "ca-app-pub-6938332798224330/6206234808"
        interstitial.loadRequest(GADRequest())
        return interstitial
    }
    
    
    class func displayInterstitial(view : UIViewController) {
        if let isReady = ads.interstitial?.isReady {
            ads.interstitial?.presentFromRootViewController(view)
        }
        else{
            println("interstitial ad is not Ready\ncontinue")
            ads.interstitial?=createAndLoadInterstitial()
        }
    }
    
    class func interstitialDidReceiveAd(ad: GADInterstitial!) -> Bool{
        println("interstitialDidReceiveAd")
        return true
    }
    
    //Interstitial delegate
    class func interstitial(ad: GADInterstitial!, didFailToReceiveAdWithError error: GADRequestError!) -> Bool{
        println("interstitialDidFailToReceiveAdWithError:\(error.localizedDescription)")
        ads.interstitial? = createAndLoadInterstitial()
        return false
    }
    
    
}