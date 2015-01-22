//
//  general.swift
//  app
//
//  Created by Asaf Horovitz on 12/11/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

import UIKit
import Foundation
import SystemConfiguration


class general{
    
    struct MyVariables {
        static var contest = ""
        static var contestData : NSDictionary!
        static var jsonURLResult : NSDictionary!
        static var imageCache = NSMutableDictionary()
    }
    
    struct contest{
        static var subtypesArray : NSArray!
        static var link = ""
    }
    
    class func isConnectedToNetwork() -> Bool {
        
        var zeroAddress = sockaddr_in(sin_len: 0, sin_family: 0, sin_port: 0, sin_addr: in_addr(s_addr: 0), sin_zero: (0, 0, 0, 0, 0, 0, 0, 0))
        zeroAddress.sin_len = UInt8(sizeofValue(zeroAddress))
        zeroAddress.sin_family = sa_family_t(AF_INET)
        
        let defaultRouteReachability = withUnsafePointer(&zeroAddress) {
            SCNetworkReachabilityCreateWithAddress(nil, UnsafePointer($0)).takeRetainedValue()
        }
        
        var flags: SCNetworkReachabilityFlags = 0
        if SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags) == 0 {
            return false
        }
        
        let isReachable = (flags & UInt32(kSCNetworkFlagsReachable)) != 0
        let needsConnection = (flags & UInt32(kSCNetworkFlagsConnectionRequired)) != 0
        let connected=(isReachable && !needsConnection) ? true : false
        println("internet connection available: " + connected.description)
        return connected
    }
    
    class func getDataFromJsonFile(success: ((data: NSData) -> Void)) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
            
            let filePath = NSBundle.mainBundle().pathForResource("contests",ofType:"json")
            var readError:NSError?
            let data = NSData(contentsOfFile:filePath!,
                options: NSDataReadingOptions.DataReadingUncached,
                error:&readError)         })
    }
    
    class func loadDataFromURL(url: NSURL, completion:(data: NSData?, error: NSError?) -> Void) {
        var session = NSURLSession.sharedSession()
        
        // Use NSURLSession to get data from an NSURL
        let loadDataTask = session.dataTaskWithURL(url, completionHandler: { (data: NSData!, response: NSURLResponse!, error: NSError!) -> Void in
            if let responseError = error {
                completion(data: nil, error: responseError)
            } else if let httpResponse = response as? NSHTTPURLResponse {
                if httpResponse.statusCode != 200 {
                    var statusError = NSError(domain:"com.raywenderlich", code:httpResponse.statusCode, userInfo:[NSLocalizedDescriptionKey : "HTTP status code has unexpected value."])
                    completion(data: nil, error: statusError)
                } else {
                    completion(data: data, error: nil)
                }
            }
        })
        loadDataTask.resume()
    }
    
    class func setContestsFromURL() {
        println("fetching contests from URL")
        let urlPath = "https://s3-us-west-2.amazonaws.com/pickarace/contests.json"
        let url = NSURL(string: urlPath)
        let session = NSURLSession.sharedSession()
        let task = session.dataTaskWithURL(url!, completionHandler: {data, response, error -> Void in
            if(error != nil) {
                // If there is an error in the web request, print it to the console
                println(error.localizedDescription)
            }
            var err: NSError?
            general.MyVariables.jsonURLResult = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error: &err) as NSDictionary
            if(err != nil) {
                // If there is an error parsing JSON, print it to the console
                println("JSON Error \(err!.localizedDescription)")
            }
        })
        task.resume()
    }
    
    
    class func loadImagesfromURL() -> Void {
        for event in MyVariables.jsonURLResult["events"] as NSArray{
            let vendor: NSDictionary = event["vendor"] as NSDictionary
            let vendorName: String = vendor["name"] as String
            let urlString = "https://s3-us-west-2.amazonaws.com/pickarace/"+vendorName+".png"
            println("downloading file: "+urlString)
          
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
                // Jump in to a background thread to get the image for this item
                
                 var image: UIImage? = MyVariables.imageCache.valueForKey(urlString) as? UIImage
                // Check our image cache for the existing key. This is just a dictionary of UIImages
                
                
                if(image == nil) {
                    // If the image does not exist, we need to download it
                    var imgURL: NSURL = NSURL(string: urlString)!
                    
                   
                    var request: NSURLRequest = NSURLRequest(URL: imgURL)
                    var urlConnection: NSURLConnection = NSURLConnection(request: request, delegate: self)!
                    NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue.mainQueue(), completionHandler: {(response: NSURLResponse!,data: NSData!,error: NSError!) -> Void in
                        if (error == nil) {
                            image = UIImage(data: data)
                            
                            // Store the image in to our cache
                            MyVariables.imageCache.setValue(image, forKey: urlString)
                        }
                        else {
                             println("Error: \(error.localizedDescription)")
                        }
                    })
                    
                }
                else {
                    println("image already exists")
                }
                
                
            })
        }
    }
}
