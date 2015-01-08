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
        println("trying to load interstitial ad")
        var interstitial = GADInterstitial()
        interstitial.adUnitID = "ca-app-pub-6535762882222672/7951508541"
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
    
    class func loadBanners(viewcontroller : UIViewController){
        var bannerView = GADBannerView(adSize: kGADAdSizeBanner)
        bannerView?.adUnitID = "ca-app-pub-6535762882222672/6474775341"
        bannerView?.rootViewController = viewcontroller
        var bannerFrame = bannerView!.frame
        
        
        let screenRect = UIScreen.mainScreen().bounds
        let screenHeight = screenRect.size.height
        bannerFrame.origin.y = screenHeight - bannerFrame.size.height
        bannerFrame.origin.x = 25
        bannerView!.frame = bannerFrame
        
        viewcontroller.view.addSubview(bannerView!)
        var request:GADRequest = GADRequest()
        bannerView?.loadRequest(request)
  /*      var timer:NSTimer?
        timer?.invalidate()
        timer = NSTimer.scheduledTimerWithTimeInterval(20, target: viewcontroller, selector: "GoogleAdRequestTimer", userInfo: nil, repeats: true)
    */}
}